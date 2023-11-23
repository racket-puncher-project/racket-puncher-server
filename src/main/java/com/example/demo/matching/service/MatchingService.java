package com.example.demo.matching.service;

import com.example.demo.entity.Matching;
import com.example.demo.matching.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MatchingService {
    Matching create(String email, MatchingDetailRequestDto matchingDetailRequestDto);
    Matching update(String email, Long matchingId, MatchingDetailRequestDto matchingDetailRequestDto);
    void delete(String email, Long matchingId);
    Page<MatchingPreviewDto> findFilteredMatching(FilterRequestDto filterRequestDto, Pageable pageable);
    Page<MatchingPreviewDto> findCloseMatching(LocationDto locationDto, Double distance, Pageable pageable);
    MatchingDetailResponseDto getDetail(Long matchingId);
    ApplyContents getApplyContents(String email, long matchId) throws JsonProcessingException;
}