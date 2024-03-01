package com.example.demo.scheduler;

import static com.example.demo.type.PrecipitationType.*;
import static com.example.demo.util.dateformatter.DateFormatter.formForDate;
import static com.example.demo.util.dateformatter.DateFormatter.formForDateTime;
import static com.example.demo.util.dateformatter.DateFormatter.formForTime;

import com.example.demo.apply.repository.ApplyRepository;
import com.example.demo.chat.service.ChatNotificationService;
import com.example.demo.entity.Apply;
import com.example.demo.entity.Matching;
import com.example.demo.matching.repository.MatchingRepository;
import com.example.demo.notification.repository.NotificationRepository;
import com.example.demo.notification.service.NotificationService;
import com.example.demo.openfeign.service.weather.WeatherService;
import com.example.demo.scheduler.dto.DateTimeInfo;
import com.example.demo.type.ApplyStatus;
import com.example.demo.type.NotificationType;
import com.example.demo.type.RecruitStatus;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {
    private final MatchingRepository matchingRepository;
    private final NotificationService notificationService;
    private final ApplyRepository applyRepository;
    private final WeatherService weatherService;
    private final NotificationRepository notificationRepository;
    private final ChatNotificationService chatNotificationService;

    @Async
    public CompletableFuture<DateTimeInfo> getTimes() {
        String now = LocalDateTime.now().format(formForDateTime);
        String date = LocalDate.now().format(formForDate);
        String time = LocalTime.now().format(formForTime);

        LocalDateTime recruitDueDateTime = LocalDateTime.parse(now, formForDateTime);
        LocalDate today = LocalDate.parse(date, formForDate);
        LocalTime currentTime = LocalTime.parse(time, formForTime);
        DateTimeInfo dateTimeInfo = DateTimeInfo.builder()
                .recruitDueDateTime(recruitDueDateTime)
                .today(today)
                .currentTime(currentTime)
                .build();
        return CompletableFuture.completedFuture(dateTimeInfo);
    }

    @Scheduled(cron = "${scheduler.cron.matches.confirm}")
    public void confirmResultsOfMatchesAtDueDate() {
        DateTimeInfo dateTimeInfo = getTimes().join();
        confirmResultsOfMatches(dateTimeInfo);
        log.info("Schedule for confirming matching is finished at  " + LocalDateTime.now().format(formForDateTime));
    }

    @Transactional
    public void confirmResultsOfMatches(DateTimeInfo dateTimeInfo) {
        List<Matching> matchesForConfirm
                = matchingRepository.findAllByRecruitDueDateTime(dateTimeInfo.getRecruitDueDateTime());

        List<Matching> confirmedMatchesForFinish
                = matchingRepository
                .findAllByRecruitStatusFinished(RecruitStatus.CONFIRMED, dateTimeInfo.getToday(), dateTimeInfo.getCurrentTime());

        List<Matching> weatherIssueMatchesForFinish
                = matchingRepository
                .findAllByRecruitStatusFinished(RecruitStatus.WEATHER_ISSUE, dateTimeInfo.getToday(), dateTimeInfo.getCurrentTime());

        List<Matching> matchesEndedWithinOneHour
                = matchingRepository.findAllWithEndTimeWithinLastHour();

        if (!CollectionUtils.isEmpty(matchesForConfirm)) {
            changeStatusOfMatches(matchesForConfirm);
        }

        if (!CollectionUtils.isEmpty(confirmedMatchesForFinish)) {
            changeRecruitStatusesToFinished(confirmedMatchesForFinish);
        }

        if (!CollectionUtils.isEmpty(weatherIssueMatchesForFinish)) {
            changeRecruitStatusIfMatchingFinished(weatherIssueMatchesForFinish);
        }

        if (!CollectionUtils.isEmpty(matchesEndedWithinOneHour)) {
            sendChatRoomWillClose(matchesEndedWithinOneHour);
        }
    }

    private void changeRecruitStatusesToFinished(List<Matching> matchesForFinish) {
        matchesForFinish.forEach(this::changeRecruitStatusToFinished);
    }

    private void changeRecruitStatusIfMatchingFinished(List<Matching> weatherIssueMatches) {
        weatherIssueMatches.forEach(matching -> {
            if (Objects.equals(matching.getRecruitNum(), matching.getAcceptedNum())) {
                changeRecruitStatusToFinished(matching);
            }
        });

    }

    private void changeRecruitStatusToFinished(Matching matching) {
        matching.changeRecruitStatus(RecruitStatus.FINISHED);
        log.info("matching finished -> " + matching.getId());

        var applies = applyRepository
                .findAllByMatching_IdAndApplyStatus(matching.getId(), ApplyStatus.ACCEPTED);

        for (Apply apply : applies) {
            notificationService.createNotification(apply.getSiteUser(), matching,
                    NotificationType.MATCHING_FINISHED);
        }
    }

    private void changeStatusOfMatches(List<Matching> matchesForConfirm) {
        matchesForConfirm
                .forEach(matching
                        -> {
                    var applies = applyRepository
                            .findAllByMatching_IdAndApplyStatus(matching.getId(), ApplyStatus.ACCEPTED);

                    if (RecruitStatus.FULL.equals(matching.getRecruitStatus())) {
                        matching.changeRecruitStatus(RecruitStatus.CONFIRMED);
                        log.info("matching succeed -> " + matching.getId());

                        for (Apply apply : applies) {
                            notificationService.createNotification(apply.getSiteUser(), matching,
                                    NotificationType.MATCHING_CLOSED);
                        }
                    }
                    if (RecruitStatus.OPEN.equals(matching.getRecruitStatus())) {
                        matching.changeRecruitStatus(RecruitStatus.FAILED);
                        log.info("matching failed -> " + matching.getId());
                        for (Apply apply : applies) {
                            notificationService.createNotification(apply.getSiteUser(), matching,
                                    NotificationType.MATCHING_FAILED);
                        }
                    }
                });
    }

    @Transactional
    @Scheduled(cron = "${scheduler.cron.weather.notification}") // 매일 새벽 6시 30분에 수행
    public void checkWeatherAndSendNotification() {
        String now = LocalDate.now().format(formForDate);
        LocalDate matchingDate = LocalDate.parse(now, formForDate);
        log.info("scheduler for weather notification is started at " + now);

        List<Matching> matchesForWeatherNotification
                = matchingRepository.findAllByDate(matchingDate);

        if (!CollectionUtils.isEmpty(matchesForWeatherNotification)) {
            saveWeatherNotification(matchesForWeatherNotification);
        }
    }

    @Transactional
    @Scheduled(cron = "${scheduler.cron.notification.delete}") // 매일 00:30분에 수행
    public void deleteNotifications() {
        LocalDateTime threeDaysBeforeNow = LocalDateTime.now().minusDays(3);
        log.info("scheduler for notification deleting is started at " + LocalDateTime.now()
                .format(formForDateTime));

        notificationRepository.deleteAllByCreateTimeBefore(threeDaysBeforeNow);
    }

    private void saveWeatherNotification(List<Matching> matchesForWeatherNotification) {
        matchesForWeatherNotification.forEach(
                matching -> {
                    var weatherDto = weatherService.getWeatherResponseDtoByMatching(matching);

                    var applies = applyRepository
                            .findAllByMatching_IdAndApplyStatus(matching.getId(), ApplyStatus.ACCEPTED);

                    if (!NICE.equals(weatherDto.getPrecipitationType())) {
                        log.info("강수 확률: " + weatherDto.getPrecipitationProbability()
                                + ", 예상 날씨: " + weatherDto.getPrecipitationType().getMessage());
                        matching.changeRecruitStatus(RecruitStatus.WEATHER_ISSUE);

                        for (Apply apply : applies) {
                            notificationService.createNotification(apply.getSiteUser(), matching,
                                    NotificationType.makeWeatherIssueMessage(weatherDto));
                            return;
                        }
                    }

                    for (Apply apply : applies) {
                        notificationService.createNotification(apply.getSiteUser(), matching,
                                NotificationType.makeWeatherMessage());
                    }
                }
        );
    }

    private void sendChatRoomWillClose(List<Matching> matchesEndedWithinOneHour) {
        matchesEndedWithinOneHour.forEach(
                m -> chatNotificationService.notifyChatRoomWillClose(String.valueOf(m.getId())));
    }
}