package com.example.demo.apply.controller;

import com.example.demo.apply.dto.PendingAppliesAndAcceptedApplies;
import com.example.demo.apply.service.ApplyService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/matches/{matchingId}")
    public void apply(@PathVariable(value = "matchingId") long matchingId, Principal principal) {

        String email = principal.getName();

        applyService.apply(email, matchingId);
    }

    @DeleteMapping("/{applyId}")
    public void cancelApply(@PathVariable(value = "applyId") long applyId) {

        applyService.cancel(applyId);
    }

    @PatchMapping("/matches/{matchingId}")
    public void acceptApply(@RequestBody PendingAppliesAndAcceptedApplies pendingAppliesAndAcceptedApplies,
                            @PathVariable(value = "matchingId") long matchingId, Principal principal) {

        var email = principal.getName();
        List<Long> pendingApplies = pendingAppliesAndAcceptedApplies.getPendingApplies();
        List<Long> acceptedApplies = pendingAppliesAndAcceptedApplies.getAcceptedApplies();

        applyService.accept(email, pendingApplies, acceptedApplies, matchingId);
    }
}
