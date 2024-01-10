package com.example.demo.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PenaltyType {
    MATCHING_MODIFY(-3),
    MATCHING_DELETE(-5),
    CANCEL_APPLY(-5);

    private final int score;
}