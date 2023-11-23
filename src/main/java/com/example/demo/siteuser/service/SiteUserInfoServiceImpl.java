package com.example.demo.siteuser.service;

import com.example.demo.apply.repository.ApplyRepository;
import com.example.demo.entity.*;
import com.example.demo.notification.repository.NotificationRepository;
import com.example.demo.siteuser.dto.*;
import com.example.demo.matching.repository.MatchingRepository;
import com.example.demo.siteuser.repository.PenaltyScoreRepository;
import com.example.demo.siteuser.repository.ReportUserRepository;
import com.example.demo.siteuser.repository.ReviewRepository;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.PenaltyCode;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SiteUserInfoServiceImpl implements SiteUserInfoService {
    private final SiteUserRepository siteUserRepository;
    private final MatchingRepository matchingRepository;
    private final ApplyRepository applyRepository;
    private final PenaltyScoreRepository penaltyScoreRepository;
    private final ReportUserRepository reportUserRepository;
    private final NotificationRepository notificationRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SiteUserInfoServiceImpl(SiteUserRepository siteUserRepository, MatchingRepository matchingRepository, ApplyRepository applyRepository, PenaltyScoreRepository penaltyScoreRepository, ReportUserRepository reportUserRepository, NotificationRepository notificationRepository, ReviewRepository reviewRepository, PasswordEncoder passwordEncoder) {
        this.siteUserRepository = siteUserRepository;
        this.matchingRepository = matchingRepository;
        this.applyRepository = applyRepository;
        this.penaltyScoreRepository = penaltyScoreRepository;
        this.reportUserRepository = reportUserRepository;
        this.notificationRepository = notificationRepository;
        this.reviewRepository = reviewRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SiteUserInfoDto getSiteUserInfoById(Long userId) {
        SiteUser siteUser = siteUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        return SiteUserInfoDto.fromEntity(siteUser);
    }

    @Override
    public SiteUserMyInfoDto getSiteUserMyInfoById(Long userId) {
        SiteUser siteUser = siteUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        return SiteUserMyInfoDto.fromEntity(siteUser);
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

    @Transactional
    @Override
    public void updateProfileImage(Long userId, String imageUrl) {
        SiteUser siteUser = siteUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        siteUser.setProfileImg(imageUrl);
        siteUserRepository.save(siteUser);
    }

    @Transactional
    @Override
    public void updateSiteUserInfo(Long userId, SiteUserModifyDto siteUserModifyDto) {
        SiteUser siteUser = siteUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        updateSiteUserFromDto(siteUser, siteUserModifyDto);
        siteUserRepository.save(siteUser);
    }

    private void updateSiteUserFromDto(SiteUser siteUser, SiteUserModifyDto dto) {
        if (dto.getNickname() != null) {
            siteUser.setNickname(dto.getNickname());
        }
        if (dto.getAddress() != null) {
            siteUser.setAddress(dto.getAddress());
        }
        if (dto.getZipCode() != null) {
            siteUser.setZipCode(dto.getZipCode());
        }
        if (dto.getNtrp() != null) {
            siteUser.setNtrp(dto.getNtrp());
        }
        if (dto.getGender() != null) {
            siteUser.setGender(dto.getGender());
        }
        if (dto.getAgeGroup() != null) {
            siteUser.setAgeGroup(dto.getAgeGroup());
        }
        if (dto.getPassword() != null) {
            siteUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getEmail() != null) {
            siteUser.setEmail(dto.getEmail());
        }
        if (dto.getPhoneNumber() != null) {
            siteUser.setPhoneNumber(dto.getPhoneNumber());
        }

        if (dto.getProfileImg() != null) {
            siteUser.setProfileImg(dto.getProfileImg());
        }
    }

    @Override
    public String getProfileUrl(Long userId) {
        SiteUser siteUser = siteUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        return siteUser.getProfileImg();
    }

    @Override
    public List<SiteUserNotificationDto> getNotificationBySiteUser(Long userId) {
        List<Notification> notificationList = notificationRepository.findBySiteUser_Id(userId);

        if (notificationList != null && !notificationList.isEmpty()) {
            return notificationList.stream()
                    .map(SiteUserNotificationDto::fromEntity)
                    .collect(Collectors.toList());
        } else {
            throw new EntityNotFoundException("No notification data found for user with ID: " + userId);
        }
    }

    @Override
    public void deleteNotification(Long userId, Long notificationId) {
        notificationRepository.deleteByIdAndSiteUser_Id(notificationId, userId);
    }

    @Override
    @Transactional
    public void updatePenaltyScore(Long userId, PenaltyCode penaltyCode) {
        SiteUser user = siteUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        int penaltyAmount = penaltyCode == PenaltyCode.OFFENSE_CHAT ||
                penaltyCode == PenaltyCode.DELETE_MATCH_EVEN_SOMEONE_APPLIED ? -10 : -20;

        user.setPenaltyScore(user.getPenaltyScore() == null ? penaltyAmount : user.getPenaltyScore() + penaltyAmount);

        PenaltyScore penaltyScore = new PenaltyScore();
        penaltyScore.setSiteUser(user);
        penaltyScore.setCode(penaltyCode);
        penaltyScore.setScore(penaltyAmount);
        penaltyScore.setCreateTime(new Timestamp(System.currentTimeMillis()));

        penaltyScoreRepository.save(penaltyScore);

        siteUserRepository.save(user);
    }

    @Override
    @Transactional
    public void createReportUser(ReportUserDto reportUserDto) {
//        // 현재 로그인한 유저의 정보 가져오기
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Object principal = authentication.getPrincipal();
//
//        String currentUseremail;
//
//        if (principal instanceof UserDetails) {
//            currentUseremail = ((UserDetails) principal).getUsername();
//        } else {
//            currentUseremail = principal.toString();
//        }
//
//        // 로그인한 유저의 정보로 조회
//        SiteUser siteUser = siteUserRepository.findByEmail(currentUseremail)
//                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + currentUseremail));
        SiteUser reportingUser = siteUserRepository.findById(reportUserDto.getReportingUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with user id: " + reportUserDto.getReportingUserId()));

        SiteUser reportinedUser = siteUserRepository.findById(reportUserDto.getReportedUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with user id: " + reportUserDto.getReportedUserId()));

        ReportUser reportUser = new ReportUser();
        reportUser.setReportingUser(reportingUser);
        reportUser.setTitle(reportUserDto.getTitle());
        reportUser.setContent(reportUserDto.getContent());
        reportUser.setReportedUser(reportinedUser);
        reportUser.setCreateTime(LocalDateTime.now());

        reportUserRepository.save(reportUser);
    }

    @Override
    public List<ViewReportsDto> getAllReports() {
        List<ReportUser> reportUsers = reportUserRepository.findAll();
        if (reportUsers.isEmpty()) {
            throw new EntityNotFoundException("No reports found");
        }
        return reportUsers.stream()
                .map(ViewReportsDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void processReviewCheckboxes(Long matchingId, Long objectUserId, ReviewCheckboxDto reviewDto) {
        SiteUser objectSiteUser = siteUserRepository.findById(objectUserId)
                .orElseThrow(() -> new EntityNotFoundException("ObjectSiteUser not found with id: " + objectUserId));
        Matching matching = matchingRepository.findById(matchingId)
                .orElseThrow(() -> new EntityNotFoundException("Matching not found with id: " + matchingId));

        int positiveCount = countTrueCheckboxes(
                reviewDto.isPositive1(), reviewDto.isPositive2(), reviewDto.isPositive3(),
                reviewDto.isPositive4(), reviewDto.isPositive5());

        int negativeCount = countTrueCheckboxes(
                reviewDto.isNegative1(), reviewDto.isNegative2(), reviewDto.isNegative3(),
                reviewDto.isNegative4(), reviewDto.isNegative5()) * -1;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String currentUseremail;

        if (principal instanceof UserDetails) {
            currentUseremail = ((UserDetails) principal).getUsername();
        } else {
            currentUseremail = principal.toString();
        }

        SiteUser siteUser = siteUserRepository.findByEmail(currentUseremail)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + currentUseremail));

        Review review = new Review();
        review.setMatching(matching);
        review.setObjectUser(objectSiteUser);
        review.setSubjectUser(siteUser);
        review.setPositiveScore(positiveCount);
        review.setNegativeScore(negativeCount);
        review.setScore(positiveCount + negativeCount);
        review.setCreateTime(LocalDateTime.now());
        reviewRepository.save(review);

        objectSiteUser.setMannerScore(objectSiteUser.getMannerScore() + (positiveCount + negativeCount) / matching.getConfirmedNum());
        siteUserRepository.save(objectSiteUser);
    }

    private int countTrueCheckboxes(boolean... checkboxes) {
        int count = 0;
        for (boolean checkbox : checkboxes) {
            if (checkbox) {
                count++;
            }
        }
        return count;
    }
}