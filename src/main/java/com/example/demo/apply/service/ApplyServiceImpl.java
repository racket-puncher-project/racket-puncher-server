package com.example.demo.apply.service;

import static com.example.demo.exception.type.ErrorCode.*;

import com.example.demo.apply.dto.ApplyDto;
import com.example.demo.apply.repository.ApplyRepository;
import com.example.demo.common.FindEntity;
import com.example.demo.entity.Apply;
import com.example.demo.entity.Matching;
import com.example.demo.exception.RacketPuncherException;
import com.example.demo.notification.service.NotificationService;
import com.example.demo.openfeign.service.weather.WeatherService;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.ApplyStatus;
import com.example.demo.type.NotificationType;
import com.example.demo.type.PenaltyType;
import com.example.demo.type.RecruitStatus;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplyServiceImpl implements ApplyService {

    private final ApplyRepository applyRepository;
    private final SiteUserRepository siteUserRepository;
    private final NotificationService notificationService;
    private final FindEntity findEntity;
    private final WeatherService weatherService;
    private static final DateTimeFormatter formForDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    @Transactional
    public Apply apply(String email, long matchingId) {
        var user = siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new RacketPuncherException(USER_NOT_FOUND));
        var matching = findEntity.findMatching(matchingId);
        var organizer = matching.getSiteUser();

        validateRecruitNotConfirmed(matching);

        if (isAlreadyExisted(user.getId(), matchingId)) {
            var existApply = applyRepository.findBySiteUser_IdAndMatching_Id(user.getId(), matchingId).get();
            validateApplyDuplication(existApply);
            existApply.changeApplyStatus(ApplyStatus.PENDING); // 취소 신청 내역 있을 경우 상태만 변경
            return existApply;
        }

        var applyDto = ApplyDto.builder()
                .matching(matching)
                .siteUser(user)
                .build();

        var apply = applyRepository.save(Apply.fromDto(applyDto));
        notificationService.createAndSendNotification(organizer, matching, NotificationType.REQUEST_APPLY);

        if (!LocalDate.now().format(formForDate).equals(matching.getDate())) {
            return apply;
        }
        var weatherDto = weatherService.getWeatherResponseDtoByMatching(matching);

        if (weatherDto != null) {
            matching.changeRecruitStatus(RecruitStatus.WEATHER_ISSUE);
            notificationService.createAndSendNotification(user, matching,
                    NotificationType.makeWeatherIssueMessage(weatherDto));
            return apply;
        }
        notificationService.createAndSendNotification(user, matching,
                NotificationType.makeWeatherMessage());
        return apply;
    }

    private boolean isAlreadyExisted(long userId, long matchingId) {
        return applyRepository.existsBySiteUser_IdAndMatching_Id(userId, matchingId);
    }

    private static void validateApplyDuplication(Apply existApply) {

        if (!ApplyStatus.CANCELED.equals(existApply.getApplyStatus())) {
            throw new RacketPuncherException(APPLY_ALREADY_EXISTED);
        }
    }

    private static void validateRecruitNotConfirmed(Matching matching) {
        if (RecruitStatus.CONFIRMED.equals(matching.getRecruitStatus())) {
            throw new RacketPuncherException(MATCHING_ALREADY_CONFIRMED);
        }
    }

    @Override
    @Transactional
    public Apply cancel(long applyId) {
        var apply = findEntity.findApply(applyId);

        validateCancelDuplication(apply);

        var matching = apply.getMatching();

        validateNotYourOwnPosting(matching, apply);
        validateRecruitNotFinished(matching);
        validateRecruitNotConfirmed(matching);

        if (ApplyStatus.ACCEPTED.equals(apply.getApplyStatus())) {
            matching.updateAcceptedNum(matching.getAcceptedNum() - 1);
        }

        if (isRecruitFullAndIsApplyAccepted(matching, apply)) {
                matching.changeRecruitStatus(RecruitStatus.OPEN);
                apply.getSiteUser().penalize(PenaltyType.CANCEL_APPLY);
        }

        apply.changeApplyStatus(ApplyStatus.CANCELED);
        notificationService.createAndSendNotification(matching.getSiteUser(),
                matching, NotificationType.CANCEL_APPLY);
        return apply;
    }

    private static boolean isRecruitFullAndIsApplyAccepted(Matching matching, Apply apply) {
        return RecruitStatus.FULL.equals(matching.getRecruitStatus())
                && ApplyStatus.ACCEPTED.equals(apply.getApplyStatus());
    }

    private static void validateRecruitNotFinished(Matching matching) {
        if (RecruitStatus.FINISHED.equals(matching.getRecruitStatus())) {
            throw new RacketPuncherException(MATCHING_ALREADY_FINISHED);
        }
    }

    private static void validateNotYourOwnPosting(Matching matching, Apply apply) {
        if (matching.getSiteUser().equals(apply.getSiteUser())) {
            throw new RacketPuncherException(SELF_APPLY_CANCEL_DENIED);
        }
    }

    private static void validateCancelDuplication(Apply apply) {
        if (ApplyStatus.CANCELED.equals(apply.getApplyStatus())) {
            throw new RacketPuncherException(APPLY_ALREADY_CANCELED);
        }
    }

    @Override
    @Transactional
    public Matching accept(String email, List<Long> pendingApplies, List<Long> acceptedApplies, long matchingId) {
        var matching = findEntity.findMatching(matchingId);
        validateOrganizer(email, matching);

        var recruitNum = matching.getRecruitNum();
        var acceptedNum = acceptedApplies.size();

        validateOverRecruitNumber(recruitNum, acceptedNum);

        pendingApplies
                .forEach(pendingId
                        -> findEntity.findApply(pendingId).changeApplyStatus(ApplyStatus.PENDING));

        acceptedApplies
                .forEach(acceptedId
                        -> {
                    findEntity.findApply(acceptedId).changeApplyStatus(ApplyStatus.ACCEPTED);
                    notificationService
                            .createAndSendNotification(findEntity.findApply(acceptedId).getSiteUser(),
                                    matching, NotificationType.ACCEPT_APPLY);
                });

        matching.updateAcceptedNum(acceptedNum);
        checkForRecruitStatusChanging(recruitNum, acceptedNum, matching);
        return matching;
    }

    private static void validateOrganizer(String email, Matching matching) {
        if (!matching.getSiteUser().getEmail().equals(email)) {
            throw new RacketPuncherException(PERMISSION_DENIED_TO_ACCEPTED_APPLIES);
        }
    }

    private static void checkForRecruitStatusChanging(Integer recruitNum, int acceptedNum, Matching matching) {
        if (recruitNum == acceptedNum) {
            matching.changeRecruitStatus(RecruitStatus.FULL);
        }
    }

    private static void validateOverRecruitNumber(int recruitNum, int acceptedNum) {
        if (acceptedNum > recruitNum) {
            throw new RacketPuncherException(RECRUIT_NUMBER_OVERED);
        }
    }
}