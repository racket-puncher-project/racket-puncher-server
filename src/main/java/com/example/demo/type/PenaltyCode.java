package com.example.demo.type;

public enum PenaltyCode {
    OFFENSE_CHAT(0),
    DELETE_MATCH_EVEN_SOMEONE_APPLIED(1),
    DELETE_MATCH_AFTER_CONFIRM(2);

    private final int code;

    PenaltyCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static PenaltyCode fromString(String text) {
        for (PenaltyCode b : PenaltyCode.values()) {
            if (b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}