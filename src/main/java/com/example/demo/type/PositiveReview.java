package com.example.demo.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PositiveReview {
    KIND(2),
    PUNCTUAl(2),
    GOOD_MANNER(2),
    HELPFUL(2),
    PROACTIVE(2);

    private final int score;
}