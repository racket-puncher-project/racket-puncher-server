package com.example.demo.apply.repository;

import com.example.demo.entity.Apply;

import com.example.demo.type.ApplyStatus;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyRepository extends JpaRepository<Apply, Long> {
    boolean existsBySiteUser_IdAndMatching_Id(long userId, long matchingId);

    Optional<Apply> findBySiteUser_IdAndMatching_Id(long userId, long matchingId);

    Optional<List<Apply>> findAllByMatching_IdAndApplyStatus(long matchingId, ApplyStatus applyStatus);

    Optional<Integer> countByMatching_IdAndApplyStatus(long matchingId, ApplyStatus applyStatus);
  
    List<Apply> findAllBySiteUser_Id(Long userId);

    Optional<List<Apply>> findAllByMatching_Id(long matchingId);
}
