package com.example.demo.apply.controller;

import com.example.demo.apply.dto.AppliedListAndConfirmedList;
import com.example.demo.apply.service.ApplyService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/apply")
@RequiredArgsConstructor
public class ApplyController {

    private final ApplyService applyService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/matches/{match_id}")
    public void apply(@PathVariable(value = "match_id") long matchingId, Principal principal) {

        String email = principal.getName();

        applyService.apply(email, matchingId);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{apply_id}")
    public void cancelApply(@PathVariable(value = "apply_id") long applyId) {

        applyService.cancel(applyId);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/matches/{matching_id}")
    public void acceptApply(@RequestBody AppliedListAndConfirmedList appliedListAndConfirmedList,
                                   @PathVariable(value = "matching_id") long matchingId) {

        List<Long> appliedList = appliedListAndConfirmedList.getAppliedList();
        List<Long> confirmedList = appliedListAndConfirmedList.getConfirmedList();

        applyService.accept(appliedList, confirmedList, matchingId);
    }
}
