package com.example.demo.common;

import com.example.demo.apply.repository.ApplyRepository;
import com.example.demo.entity.Apply;
import com.example.demo.entity.Matching;
import com.example.demo.entity.SiteUser;
import com.example.demo.exception.impl.ApplyNotFoundException;
import com.example.demo.exception.impl.MatchingNotFoundException;
import com.example.demo.exception.impl.UserNotFoundException;
import com.example.demo.matching.repository.MatchingRepository;
import com.example.demo.siteuser.repository.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindEntity {

    private final MatchingRepository matchingRepository;
    private final SiteUserRepository siteUserRepository;
    private final ApplyRepository applyRepository;

    public Matching findMatching(long matchingId) {
        return matchingRepository.findById(matchingId).orElseThrow(
                () -> new MatchingNotFoundException());
    }

    public SiteUser findUser(long userId) {
        return siteUserRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException());
    }

    public Apply findApply(long applyId) {
        return applyRepository.findById(applyId)
                .orElseThrow(() -> new ApplyNotFoundException());
    }
}
