package com.example.demo.scheduler;

import com.example.demo.apply.repository.ApplyRepository;
import com.example.demo.entity.Apply;
import com.example.demo.entity.Matching;
import com.example.demo.matching.repository.MatchingRepository;
import com.example.demo.notification.dto.LocationAndDateFromMatching;
import com.example.demo.notification.service.NotificationService;
import com.example.demo.openfeign.service.weather.WeatherService;
import com.example.demo.type.ApplyStatus;
import com.example.demo.type.NotificationType;
import com.example.demo.type.RecruitStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class Scheduler {
    private final MatchingRepository matchingRepository;
    private static final DateTimeFormatter formForDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter formForDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter formForWeather = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final NotificationService notificationService;
    private final ApplyRepository applyRepository;
    private final WeatherService weatherService;

    @Async
    @Scheduled(cron = "${scheduler.cron.matches.confirm}") // 매일 정시에 수행
    public void confirmResultsOfMatchesAtDueDate() {
        String now = LocalDateTime.now().format(formForDateTime);
        LocalDateTime recruitDueDateTime = LocalDateTime.parse(now, formForDateTime);
        log.info("scheduler is started at " + now);

        List<Matching> matchesForConfirm
                = matchingRepository.findAllByRecruitDueDateTime(recruitDueDateTime).get();

        if (!CollectionUtils.isEmpty(matchesForConfirm)) {
            changeStatusOfMatches(matchesForConfirm);
        }
    }

    private void changeStatusOfMatches(List<Matching> matchesForConfirm) {
        matchesForConfirm
                .forEach(matching
                        -> {
                    var applies = applyRepository
                            .findAllByMatching_IdAndApplyStatus(matching.getId(), ApplyStatus.ACCEPTED);

                    if (RecruitStatus.FULL.equals(matching.getRecruitStatus())) {
                        matching.changeRecruitStatus(RecruitStatus.CLOSED);
                        log.info("matching succeed -> " + matching.getId());

                        for (Apply apply : applies.get()) {
                            notificationService.createAndSendNotification(apply.getSiteUser(), matching,
                                    NotificationType.MATCHING_CLOSED);
                        }
                    } else {
                        matching.changeRecruitStatus(RecruitStatus.FAILED);
                        log.info("matching failed -> " + matching.getId());
                        for (Apply apply : applies.get()) {
                            notificationService.createAndSendNotification(apply.getSiteUser(), matching,
                                    NotificationType.MATCHING_FAILED);
                        }
                    }
                });
    }

    @Async
    @Scheduled(cron = "${scheduler.cron.weather.notification}") // 매일 새벽 6시 30분에 수행
    public void checkWeatherAndSendNotification() {
        String now = LocalDate.now().format(formForDate);
        LocalDate matchingDate = LocalDate.parse(now, formForDate);
        log.info("scheduler for weather notification is started at " + now);

        List<Matching> matchesForWeatherNotification
                = matchingRepository.findAllByDate(matchingDate).get();

        if (!CollectionUtils.isEmpty(matchesForWeatherNotification)) {
            sendWeatherNotification(matchesForWeatherNotification);
        }

    }

    private void sendWeatherNotification(List<Matching> matchesForWeatherNotification) {
        String now = LocalDateTime.now().format(formForWeather);
        matchesForWeatherNotification.forEach(
                matching -> {
                    String nx = String.valueOf((int) Math.round(matching.getLat()));
                    String ny = String.valueOf((int) Math.round(matching.getLon()));
                    var locationAndDateFromMatching
                            = LocationAndDateFromMatching.builder()
                            .baseDate(now)
                            .nx(nx)
                            .ny(ny)
                            .build();

                    var weatherDto = weatherService.getWeather(locationAndDateFromMatching);

                    if (weatherDto != null) {
                        log.info("강수확률: " + weatherDto.getPrecipitationProbability() + "강수량: " + weatherDto.getPrecipitationType().getMessage());
                        matching.changeRecruitStatus(RecruitStatus.WEATHER_ISSUE);
                        var applies = applyRepository
                                .findAllByMatching_IdAndApplyStatus(matching.getId(), ApplyStatus.ACCEPTED);
                        for (Apply apply : applies.get()) {
                            notificationService.createAndSendNotification(apply.getSiteUser(), matching,
                                    NotificationType.makeWeatherMessage(weatherDto));
                        }
                    }
                }
        );
    }
}