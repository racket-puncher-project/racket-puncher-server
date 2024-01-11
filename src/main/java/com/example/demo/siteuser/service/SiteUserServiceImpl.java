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
import com.example.demo.siteuser.dto.InputReviewDto;
import com.example.demo.siteuser.dto.MatchingMyMatchingDto;
import com.example.demo.siteuser.dto.ProcessedReviewDto;
import com.example.demo.siteuser.dto.SiteUserInfoDto;
import com.example.demo.siteuser.dto.MyInfoDto;
import com.example.demo.siteuser.dto.NotificationDto;
import com.example.demo.siteuser.dto.ReviewPageInfoDto;
import com.example.demo.siteuser.dto.UpdateSiteUserInfoDto;
import com.example.demo.siteuser.repository.ReviewRepository;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.ApplyStatus;
import com.example.demo.type.NegativeReviewType;
import com.example.demo.type.PositiveReviewType;
import jakarta.persistence.EntityNotFoundException;
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

    private static void validatePassword(UpdateSiteUserInfoDto updateSiteUserInfoDto) {
        if (!updateSiteUserInfoDto.getPassword().equals(updateSiteUserInfoDto.getCheckPassword())) {
            throw new RacketPuncherException(WRONG_PASSWORD);
        }
    }

    @Override
    public List<MatchingMyMatchingDto> getMatchingBySiteUser(Long userId) {
        List<Matching> matchingList = matchingRepository.findBySiteUser_Id(userId);

        if (matchingList != null && !matchingList.isEmpty()) {
            return matchingList.stream()
                    .map(MatchingMyMatchingDto::fromEntity)
                    .collect(Collectors.toList());
        } else {
            throw new EntityNotFoundException("No matching data found for user with ID: " + userId);
        }
    }

    @Override
    public List<MatchingMyMatchingDto> getApplyBySiteUser(Long userId) {
        List<Apply> applyList = applyRepository.findAllBySiteUser_Id(userId);

        if (applyList != null && !applyList.isEmpty()) {
            List<MatchingMyMatchingDto> matchingDtos = applyList.stream()
                    .filter(apply -> apply.getMatching() != null)
                    .map(apply -> MatchingMyMatchingDto.fromEntity(apply.getMatching()))
                    .collect(Collectors.toList());
            return matchingDtos;
        } else {
            throw new EntityNotFoundException("No matching data found for user with ID: " + userId);
        }
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

            int positiveScore = inputReviewDto.getPositiveReviewTypes()
                    .stream().mapToInt(PositiveReviewType::getScore).sum();
            int negativeScore = inputReviewDto.getNegativeReviewTypes()
                    .stream().mapToInt(NegativeReviewType::getScore).sum();

            objectUser.sumMannerScore(positiveScore, negativeScore);

            var processedReviewDto = ProcessedReviewDto.builder()
                    .matching(matching)
                    .objectUser(objectUser)
                    .subjectUser(subjectUser)
                    .positiveReviewTypes(inputReviewDto.getPositiveReviewTypes())
                    .negativeReviewTypes(inputReviewDto.getNegativeReviewTypes())
                    .score(positiveScore + negativeScore)
                    .build();

            reviewRepository.save(Review.fromDto(processedReviewDto));
            applyRepository.findBySiteUser_IdAndMatching_Id(subjectUser.getId(), matchingId).orElseThrow(()
                            -> new RacketPuncherException(APPLY_NOT_FOUND))
                    .changeApplyStatus(ApplyStatus.EVALUATION_COMPLETED);
        }
    }
}