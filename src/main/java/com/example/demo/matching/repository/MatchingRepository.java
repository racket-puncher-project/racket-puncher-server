package com.example.demo.matching.repository;

import com.example.demo.entity.Matching;
import com.example.demo.entity.SiteUser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import com.example.demo.type.RecruitStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long>, FilteringRepositoryCustom {
    List<Matching> findAllBySiteUser_Email(String email);

    boolean existsByIdAndSiteUser(Long id, SiteUser siteUser);

    Optional<List<Matching>> findAllByRecruitDueDateTime(LocalDateTime now);

    @Query("SELECT m FROM Matching m WHERE m.recruitStatus = :recruitStatus AND m.date = :date "
            + "AND (m.endTime < :endTime OR m.endTime = :endTime)")
    Optional<List<Matching>> findAllByRecruitStatusFinished(
            @Param("recruitStatus") RecruitStatus recruitStatus,
            @Param("date") LocalDate today,
            @Param("endTime") LocalTime currentTime);

    Optional<List<Matching>> findAllByDate(LocalDate today);

    Page<Matching> findByRecruitStatusAndRecruitDueDateTimeAfter(
            RecruitStatus OPEN, LocalDateTime LocalDateTime, Pageable pageable);
}