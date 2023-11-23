package com.example.demo.matching.controller;

import com.example.demo.common.ResponseDto;
import com.example.demo.common.ResponseUtil;
import com.example.demo.matching.dto.*;
import com.example.demo.openfeign.service.address.AddressService;
import com.example.demo.matching.dto.ApplyContents;
import com.example.demo.matching.dto.MatchingDetailRequestDto;
import com.example.demo.matching.dto.MatchingPreviewDto;
import com.example.demo.openfeign.dto.address.AddressResponseDto;
import com.example.demo.matching.service.MatchingService;

import java.security.Principal;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/matches")
public class MatchingController {

    private final MatchingService matchingService;
    private final AddressService addressService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public void createMatching(
            @RequestBody MatchingDetailRequestDto matchingDetailRequestDto,
            Principal principal) {

        String email = principal.getName();
        matchingService.create(email, matchingDetailRequestDto);
    }

    @GetMapping("/{matchingId}")
    public ResponseDto<MatchingDetailResponseDto> getDetailedMatching(
            @PathVariable Long matchingId) {

        MatchingDetailResponseDto result = matchingService.getDetail(matchingId);

        return ResponseUtil.SUCCESS(result);
    }

    @PatchMapping("/{matchingId}")
    @PreAuthorize("hasRole('USER')")
    public void editMatching(
            @RequestBody MatchingDetailRequestDto matchingDetailRequestDto,
            @PathVariable Long matchingId,
            Principal principal) {

        String email = principal.getName();
        matchingService.update(email, matchingId, matchingDetailRequestDto);
    }

    @DeleteMapping("/{matchingId}")
    @PreAuthorize("hasRole('USER')")
    public void deleteMatching(
            @PathVariable Long matchingId,
            Principal principal) {

        String email = principal.getName();
        matchingService.delete(email, matchingId);
    }

    @PostMapping("/list")
    public ResponseDto<Page<MatchingPreviewDto>> getMatchingList(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false) String sort,
            @RequestBody(required = false) FilterRequestDto filterRequestDto) {

        // 정렬 없을 때
        PageRequest pageRequest = PageRequest.of(page, size);

        // 등록순 정렬
        if ("register".equals(sort)) {
            pageRequest = PageRequest.of(page, size, Sort.by("createTime").ascending());
        }
        // 마감순 정렬
        else if ("due-date".equals(sort)) {
            pageRequest = PageRequest.of(page, size, Sort.by("recruitDueDateTime").ascending());
        }
        // 거리순 정렬
        else if ("distance".equals(sort)) {
            if (filterRequestDto.getLocation().getLat() != null && filterRequestDto.getLocation().getLon() != null)
                return ResponseUtil.SUCCESS(matchingService.findFilteredMatching(filterRequestDto, pageRequest));
        }

        return ResponseUtil.SUCCESS(matchingService.findFilteredMatching(filterRequestDto, pageRequest));
    }

    @PostMapping("/list/map")
    public ResponseDto<Page<MatchingPreviewDto>> getCloseMatchingList(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "3") double distance,
            @RequestBody(required = false) LocationDto locationDto) {

        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseUtil.SUCCESS(matchingService.findCloseMatching(locationDto, distance, pageRequest));
    }

    @SneakyThrows
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{matching_id}/apply")
    public ResponseDto<ApplyContents> getApplyContents(@PathVariable(value = "matching_id") long matchingId,
                                                       Principal principal) {

        String email = principal.getName();

        var result = matchingService.getApplyContents(email, matchingId);

        return ResponseUtil.SUCCESS(result);
    }

    @GetMapping("/address")
    public ResponseDto<List<AddressResponseDto>> getAddress(@RequestParam String keyword) {

        var result = addressService.getAddressService(keyword);

        return ResponseUtil.SUCCESS(result);
    }
}