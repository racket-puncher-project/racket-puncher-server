package com.example.demo.matching.controller;

import com.example.demo.common.ResponseDto;
import com.example.demo.common.ResponseUtil;
import com.example.demo.matching.dto.*;
import com.example.demo.matching.service.MatchingService;
import com.example.demo.openfeign.dto.address.AddressResponseDto;
import com.example.demo.openfeign.service.address.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/matches")
public class MatchingController {

    private final MatchingService matchingService;
    private final AddressService addressService;

    @PostMapping
    public void createMatching(
            @RequestBody MatchingDetailRequestDto matchingDetailRequestDto, Principal principal) {

        String email = principal.getName();
        matchingService.create(email, matchingDetailRequestDto);
    }

    @GetMapping("/detail/{matchingId}")
    public ResponseDto<MatchingDetailResponseDto> getDetailedMatching(@PathVariable Long matchingId) {

        MatchingDetailResponseDto result = matchingService.getDetail(matchingId);

        return ResponseUtil.SUCCESS(result);
    }

    @PatchMapping("/{matchingId}")
    public void editMatching(@RequestBody MatchingDetailRequestDto matchingDetailRequestDto,
                             @PathVariable(value = "matchingId") Long matchingId, Principal principal) {

        String email = principal.getName();
        matchingService.update(email, matchingId, matchingDetailRequestDto);
    }

    @DeleteMapping("/{matchingId}")
    public void deleteMatching(@PathVariable Long matchingId, Principal principal) {

        String email = principal.getName();
        matchingService.delete(email, matchingId);
    }

    @PostMapping("/list")
    public ResponseDto<Page<MatchingPreviewDto>> getMatchingList(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false) String sort,
            @RequestBody(required = false) FilterRequestDto filterRequestDto) {

        Sort sortOrder = Sort.unsorted();
        if ("register".equals(sort)) {
            sortOrder = Sort.by("createTime").ascending();
        } else if ("due-date".equals(sort)) {
            sortOrder = Sort.by("recruitDueDateTime").ascending();
        }

        PageRequest pageRequest = PageRequest.of(page, size, sortOrder);
        var result = matchingService.getMatchingByFilter(filterRequestDto.getFilter(), pageRequest);

        return ResponseUtil.SUCCESS(result);
    }

    @PostMapping("/list/map")
    public ResponseDto<Page<MatchingPreviewDto>> getMatchingWithinDistance(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false, defaultValue = "3") double distance,
            @RequestBody LocationDto locationDto) {

        PageRequest pageRequest = PageRequest.of(page, size);
        var result = matchingService.getMatchingWithinDistance(locationDto, distance, pageRequest);

        return ResponseUtil.SUCCESS(result);
    }

    @SneakyThrows
    @GetMapping("/{matchingId}/apply")
    public ResponseDto<ApplyContents> getApplyContents(@PathVariable(value = "matchingId") long matchingId,
                                                       Principal principal) {
        var email = principal.getName();
        var result = matchingService.getApplyContents(email, matchingId);

        return ResponseUtil.SUCCESS(result);
    }

    @GetMapping("/address")
    public ResponseDto<List<AddressResponseDto>> getAddress(@RequestParam String keyword) {

        var result = addressService.getAddressService(keyword);

        return ResponseUtil.SUCCESS(result);
    }
}