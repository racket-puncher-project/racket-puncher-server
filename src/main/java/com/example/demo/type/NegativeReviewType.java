package com.example.demo.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NegativeReviewType {
    NO_SHOW(-10),
    FALSE_MATCHING(-15),
    CYBER_BULLYING(-3),
    SPORTS_MISCONDUCT(-3),
    LATE(-2),
    AGGRESSIVE(-3),
    MAKING_UNCOMFORTABLE(-3),
    INCONSIDERATE(-3);

    private final int score;
}