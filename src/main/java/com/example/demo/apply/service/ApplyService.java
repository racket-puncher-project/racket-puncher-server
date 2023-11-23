package com.example.demo.apply.service;

import com.example.demo.entity.Apply;
import com.example.demo.entity.Matching;

import java.util.List;

public interface ApplyService {
    Apply apply(String email, long matchId);

    Apply cancel(long applyId);

    Matching accept(List<Long> appliedList, List<Long> confirmedList, long matchingId);
}
