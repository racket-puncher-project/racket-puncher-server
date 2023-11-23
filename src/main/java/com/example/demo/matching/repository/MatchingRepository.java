package com.example.demo.matching.repository;

import com.example.demo.entity.Matching;
import com.example.demo.entity.SiteUser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.demo.type.RecruitStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingRepository extends JpaRepository<Matching, Long>, FilteringRepositoryCustom {
    List<Matching> findBySiteUser_Id(Long userId);

    boolean existsByIdAndSiteUser(Long id, SiteUser siteUser);

    Optional<List<Matching>> findAllByRecruitDueDateTime(LocalDateTime now);

    Optional<List<Matching>> findAllByDate(LocalDate today);

    Page<Matching> findByRecruitStatusAndRecruitDueDateTimeAfter(RecruitStatus OPEN, LocalDateTime LocalDateTime, Pageable pageable);
}