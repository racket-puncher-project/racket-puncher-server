package com.example.demo.apply.service;

import com.example.demo.apply.dto.ApplyDto;
import com.example.demo.apply.repository.ApplyRepository;
import com.example.demo.entity.Apply;
import com.example.demo.entity.Matching;
import com.example.demo.exception.impl.AlreadyCanceledApplyException;
import com.example.demo.exception.impl.AlreadyClosedMatchingException;
import com.example.demo.exception.impl.AlreadyExistedApplyException;
import com.example.demo.exception.impl.ClosedMatchingException;
import com.example.demo.exception.impl.OverRecruitNumberException;
import com.example.demo.exception.impl.UserNotFoundException;
import com.example.demo.exception.impl.YourOwnPostingCancelException;
import com.example.demo.matching.repository.MatchingRepository;
import com.example.demo.notification.service.NotificationService;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.ApplyStatus;
import com.example.demo.type.NotificationType;
import com.example.demo.type.RecruitStatus;
import com.example.demo.common.FindEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplyServiceImpl implements ApplyService {

    private final ApplyRepository applyRepository;
    private final MatchingRepository matchingRepository;
    private final SiteUserRepository siteUserRepository;
    private final NotificationService notificationService;
    private final FindEntity findEntity;

    @Override
    public Apply apply(String email, long matchingId) {
        var user = siteUserRepository.findByEmail(email).orElseThrow(() -> new  UserNotFoundException());
        var matching = findEntity.findMatching(matchingId);
        var organizer = matching.getSiteUser();

        validateRecruitStatus(matching); // 매칭 상태 검사

        if (isAlreadyExisted(user.getId(), matchingId)) { // 신청 중복 검사
            var existApply = applyRepository.findBySiteUser_IdAndMatching_Id(user.getId(), matchingId).get();
            validateApplyDuplication(existApply);
            existApply.changeApplyStatus(ApplyStatus.PENDING); // 취소 신청 내역 있을 경우 상태만 변경
            return existApply;
        }

        var applyDto = ApplyDto.builder()
                .matching(matching)
                .siteUser(user)
                .build();

        notificationService.createAndSendNotification(organizer, matching, NotificationType.REQUEST_APPLY);

        return applyRepository.save(Apply.fromDto(applyDto));
    }

    private boolean isAlreadyExisted(long userId, long matchingId) {
        return applyRepository.existsBySiteUser_IdAndMatching_Id(userId, matchingId);
    }

    private static void validateApplyDuplication(Apply existApply) {

        if (!existApply.getApplyStatus().equals(ApplyStatus.CANCELED)) {
            throw new AlreadyExistedApplyException();
        }
    }

    private static void validateRecruitStatus(Matching matching) {
        if (matching.getRecruitStatus().equals(RecruitStatus.CLOSED)) {
            throw new ClosedMatchingException();
        }
    }

    @Override
    @Transactional
    public Apply cancel(long applyId) {
        var apply = findEntity.findApply(applyId);

        validateCancelDuplication(apply); // 취소 중복 검사

        var matching = apply.getMatching();

        validateNotYourOwnPosting(matching, apply);
        validateMatchingClosed(matching);

        if (matching.getRecruitStatus().equals(RecruitStatus.WEATHER_ISSUE)) {
            apply.changeApplyStatus(ApplyStatus.CANCELED);
            matching.changeConfirmedNum(matching.getConfirmedNum() - 1);
            notificationService.createAndSendNotification(matching.getSiteUser(),
                    matching, NotificationType.CANCEL_APPLY);
            return apply;
        }

        if (RecruitStatus.FULL.equals(matching.getRecruitStatus())) {
            if (ApplyStatus.ACCEPTED.equals(apply.getApplyStatus())) {
                //TODO: 패널티 부여
                apply.changeApplyStatus(ApplyStatus.CANCELED);
                matching.changeRecruitStatus(RecruitStatus.OPEN);
                matching.changeConfirmedNum(matching.getConfirmedNum() - 1);
                notificationService.createAndSendNotification(matching.getSiteUser(),
                        matching, NotificationType.CANCEL_APPLY);
                return apply;
            }
        }
        apply.changeApplyStatus(ApplyStatus.CANCELED);
        matching.changeConfirmedNum(matching.getConfirmedNum() - 1);
        notificationService.createAndSendNotification(matching.getSiteUser(),
                matching, NotificationType.CANCEL_APPLY);
        return apply;
    }

    private static void validateMatchingClosed(Matching matching) {
        if (matching.getRecruitStatus().equals(RecruitStatus.CLOSED)) {
            throw new AlreadyClosedMatchingException();
        }
    }

    private static void validateNotYourOwnPosting(Matching matching, Apply apply) {
        if (matching.getSiteUser().getId() == apply.getSiteUser().getId()) {
            throw new YourOwnPostingCancelException();
        }
    }

    private static void validateCancelDuplication(Apply apply) {
        if (apply.getApplyStatus().equals(ApplyStatus.CANCELED)) {
            throw new AlreadyCanceledApplyException();
        }
    }

    @Override
    @Transactional
    public Matching accept(List<Long> appliedList, List<Long> confirmedList, long matchingId) {
        var matching = findEntity.findMatching(matchingId);
        var recruitNum = matching.getRecruitNum();
        var confirmedNum = confirmedList.size();

        validateOverRecruitNumber(recruitNum, confirmedNum);

        appliedList
                .forEach(applyId
                        -> findEntity.findApply(applyId).changeApplyStatus(ApplyStatus.PENDING));

        confirmedList
                .forEach(confirmedId
                        -> {
                    findEntity.findApply(confirmedId).changeApplyStatus(ApplyStatus.ACCEPTED);
                    notificationService
                            .createAndSendNotification(
                                    findEntity.findApply(confirmedId).getSiteUser(),
                                    matching, NotificationType.ACCEPT_APPLY);

                });
        matching.updateConfirmedNum(confirmedNum);
        checkForRecruitStatusChanging(recruitNum, confirmedNum, matching);
        return matching;
    }

    private static void checkForRecruitStatusChanging(Integer recruitNum, int confirmedNum, Matching matching) {
        if (recruitNum == confirmedNum) {
            matching.changeRecruitStatus(RecruitStatus.FULL);
        }
    }

    private static void validateOverRecruitNumber(int recruitNum, int confirmedNum) {
        if (confirmedNum > recruitNum) {
            throw new OverRecruitNumberException();
        }
    }
}