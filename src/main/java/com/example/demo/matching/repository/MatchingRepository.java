package com.example.demo.matching.repository;

import com.example.demo.entity.Matching;
import com.example.demo.entity.SiteUser;
import com.example.demo.matching.repository.boundary.CustomRepositoryForBoundary;
import com.example.demo.matching.repository.filtering.CustomRepositoryForFiltering;
import com.example.demo.type.RecruitStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long>, CustomRepositoryForFiltering, CustomRepositoryForBoundary {
    List<Matching> findAllBySiteUser_Email(String email);

    boolean existsByIdAndSiteUser(Long id, SiteUser siteUser);

    List<Matching> findAllByRecruitDueDateTime(LocalDateTime now);

    @Query("SELECT m FROM Matching m WHERE m.recruitStatus = :recruitStatus AND m.date = :date "
            + "AND (m.endTime < :endTime OR m.endTime = :endTime)")
    List<Matching> findAllByRecruitStatusFinished(
            @Param("recruitStatus") RecruitStatus recruitStatus,
            @Param("date") LocalDate today,
            @Param("endTime") LocalTime currentTime);

    List<Matching> findAllByDate(LocalDate today);

    Page<Matching> findByRecruitStatusAndRecruitDueDateTimeAfter(
            RecruitStatus OPEN, LocalDateTime LocalDateTime, Pageable pageable);

    @Query("SELECT m FROM Matching m WHERE m.date = CURRENT_DATE AND (m.endTime <= CURRENT_TIME AND m.endTime > CURRENT_TIME - 1)")
    List<Matching> findAllWithEndTimeWithinLastHour();
}