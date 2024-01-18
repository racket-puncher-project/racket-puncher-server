package com.example.demo.siteuser.service;

import static com.example.demo.exception.type.ErrorCode.*;

import com.example.demo.apply.repository.ApplyRepository;
import com.example.demo.common.FindEntity;
import com.example.demo.entity.Apply;
import com.example.demo.entity.Matching;
import com.example.demo.entity.Review;
import com.example.demo.entity.SiteUser;
import com.example.demo.exception.RacketPuncherException;
import com.example.demo.matching.repository.MatchingRepository;
import com.example.demo.notification.repository.NotificationRepository;
import com.example.demo.siteuser.dto.*;
import com.example.demo.siteuser.repository.ReviewRepository;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.ApplyStatus;
import com.example.demo.type.NegativeReviewType;
import com.example.demo.type.PositiveReviewType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class SiteUserServiceImpl implements SiteUserService {
    private final SiteUserRepository siteUserRepository;
    private final MatchingRepository matchingRepository;
    private final ApplyRepository applyRepository;
    private final NotificationRepository notificationRepository;
    private final FindEntity findEntity;
    private final PasswordEncoder passwordEncoder;
    private final ReviewRepository reviewRepository;

    private static void validatePassword(UpdateSiteUserInfoDto updateSiteUserInfoDto) {
        if (!updateSiteUserInfoDto.getPassword().equals(updateSiteUserInfoDto.getCheckPassword())) {
            throw new RacketPuncherException(WRONG_PASSWORD);
        }
    }

    @Override
    public SiteUserInfoDto getSiteUserInfo(Long userId) {
        SiteUser siteUser = findEntity.findUser(userId);
        return SiteUserInfoDto.fromEntity(siteUser);
    }

    @Override
    public MyInfoDto getMyInfo(String email) {
        SiteUser siteUser = siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new RacketPuncherException(EMAIL_NOT_FOUND));
        return MyInfoDto.fromEntity(siteUser);
    }

    @Override
    @Transactional
    public SiteUser updateSiteUserInfo(String email, UpdateSiteUserInfoDto updateSiteUserInfoDto) {
        SiteUser siteUser = siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new RacketPuncherException(EMAIL_NOT_FOUND));
        if (ObjectUtils.isEmpty(updateSiteUserInfoDto.getPassword())) {
            siteUser.updateSiteUser(updateSiteUserInfoDto);
            return siteUser;
        }
        validatePassword(updateSiteUserInfoDto);
        updateSiteUserInfoDto.setPassword(passwordEncoder.encode(updateSiteUserInfoDto.getPassword()));
        siteUser.updateSiteUser(updateSiteUserInfoDto);

        return siteUser;
    }

    @Override
    public List<HostedMatchingDto> getMatchingHostedBySiteUser(String email) {
        SiteUser siteUser = siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new RacketPuncherException(EMAIL_NOT_FOUND));

        List<Matching> hostedMatchingList = matchingRepository.findAllBySiteUser_Email(email);
        List<HostedMatchingDto> hostedMatchingDtos = new ArrayList<>();
        for(Matching hostedMatching : hostedMatchingList){
            Apply apply = applyRepository.findBySiteUser_IdAndMatching_Id(siteUser.getId(), hostedMatching.getId()).get();
            hostedMatchingDtos.add(HostedMatchingDto.makeHostedMatchingDto(hostedMatching, apply.getApplyStatus(), getUsersInSameMatching(apply)));
        }
        return hostedMatchingDtos;
    }

    @Override
    public List<AppliedMatchingDto> getMatchingAppliedBySiteUser(String email) {
        SiteUser siteUser = siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new RacketPuncherException(EMAIL_NOT_FOUND));
        List<Apply> applyList = applyRepository.findAllBySiteUser_Email(email);

        return applyList.stream()
                .filter(apply -> !apply.getMatching().getSiteUser().getId().equals(siteUser.getId())) // 등록한 매칭은 제외
                .map(apply -> AppliedMatchingDto.makeAppliedMatchingDto(apply.getMatching(), apply.getApplyStatus(), getUsersInSameMatching(apply)))
                .toList();
    }

    private List<SiteUserInfoForListDto> getUsersInSameMatching(Apply apply){
        return applyRepository.findAllByMatching_Id(apply.getMatching().getId()).stream()
                .map(Apply::getSiteUser)
                .map(SiteUserInfoForListDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDto> getNotifications(String email) {
        siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new RacketPuncherException(EMAIL_NOT_FOUND));

        return notificationRepository.findAllBySiteUser_Email(email).get()
                .stream().map(notification -> NotificationDto.fromEntity(notification))
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewPageInfoDto> getReviewPageInfo(String email, Long matchingId) {
        var subjectUser = siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new RacketPuncherException(EMAIL_NOT_FOUND));

        return applyRepository
                .findAllByMatching_IdAndApplyStatus(matchingId, ApplyStatus.ACCEPTED).get()
                .stream().map(apply -> apply.getSiteUser())
                .filter(siteUser -> siteUser != subjectUser)
                .map(siteUser -> ReviewPageInfoDto.fromEntity(siteUser))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void review(String email, Long matchingId, List<InputReviewDto> inputReviewDtos) {
        var subjectUser = siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new RacketPuncherException(EMAIL_NOT_FOUND));

        var matching = findEntity.findMatching(matchingId);

        for (InputReviewDto inputReviewDto : inputReviewDtos) {
            var objectUser = findEntity.findUser(inputReviewDto.getObjectUserId());

            int positiveScore = inputReviewDto.getPositiveReviews()
                    .stream().mapToInt(PositiveReviewType::getScore).sum();
            int negativeScore = inputReviewDto.getNegativeReviews()
                    .stream().mapToInt(NegativeReviewType::getScore).sum();

            objectUser.sumMannerScore(positiveScore, negativeScore);

            var processedReviewDto = ProcessedReviewDto.builder()
                    .matching(matching)
                    .objectUser(objectUser)
                    .subjectUser(subjectUser)
                    .positiveReviews(inputReviewDto.getPositiveReviews())
                    .negativeReviews(inputReviewDto.getNegativeReviews())
                    .score(positiveScore + negativeScore)
                    .build();

            reviewRepository.save(Review.fromDto(processedReviewDto));
            applyRepository.findBySiteUser_IdAndMatching_Id(subjectUser.getId(), matchingId).orElseThrow(()
                            -> new RacketPuncherException(APPLY_NOT_FOUND))
                    .changeApplyStatus(ApplyStatus.EVALUATION_COMPLETED);
        }
    }
}