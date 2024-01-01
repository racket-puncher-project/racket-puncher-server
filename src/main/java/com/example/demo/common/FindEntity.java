package com.example.demo.common;

import static com.example.demo.exception.type.ErrorCode.*;

import com.example.demo.apply.repository.ApplyRepository;
import com.example.demo.entity.Apply;
import com.example.demo.entity.Matching;
import com.example.demo.entity.SiteUser;
import com.example.demo.exception.RacketPuncherException;
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
                () -> new RacketPuncherException(MATCHING_NOT_FOUND));
    }

    public SiteUser findUser(long userId) {
        return siteUserRepository.findById(userId).orElseThrow(
                () -> new RacketPuncherException(USER_NOT_FOUND));
    }

    public Apply findApply(long applyId) {
        return applyRepository.findById(applyId)
                .orElseThrow(() -> new RacketPuncherException(APPLY_NOT_FOUND));
    }
}
