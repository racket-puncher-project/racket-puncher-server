package com.example.demo.type;

public enum PrecipitationType {
    RAIN("1", "비"),
    RAIN_OR_SNOW("1", "눈"),
    SNOW("3", "비 혹은 눈"),
    SHOWER("4", "소나기"),
    OTHER("5", "우천");

    private String code;
    private String message;


    PrecipitationType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }


    public static PrecipitationType findPrecipitationType(String code) {
        try {
            return PrecipitationType.valueOf(code);
        } catch (IllegalArgumentException e) {
            return OTHER;
        }
    }
}
