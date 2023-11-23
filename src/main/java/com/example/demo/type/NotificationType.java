package com.example.demo.type;

import com.example.demo.openfeign.dto.weather.WeatherResponseDto;

public enum NotificationType {
    MODIFY_MATCHING("신청한 매칭글이 수정되었습니다."),
    DELETE_MATCHING("신청한 매칭글이 삭제되었습니다."),
    REQUEST_APPLY("주최한 매칭에 참가 신청을 원하는 회원이 있습니다."),
    ACCEPT_APPLY("신청한 매칭글의 주최자가 참가 신청을 수락하였습니다."),
    CANCEL_APPLY("주최한 매칭에 참가 신청을 한 회원이 참가 취소를 하였습니다."),
    WEATHER("날씨 데이터에 맞는 메시지를 생성해주세요."),
    MATCHING_CLOSED("매칭에 성공하였습니다. 채팅방이 활성화됩니다."),
    MATCHING_FAILED("매칭에 실패하였습니다.");

    private String message;


    NotificationType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public static NotificationType makeWeatherMessage(WeatherResponseDto weatherResponseDto) {
        String message = "오늘 " + weatherResponseDto.getPrecipitationType().getMessage() + "가 예상됩니다. "
                + "강수 확률은 " + weatherResponseDto.getPrecipitationProbability() + "입니다."
                + "오늘 진행 예정인 경기를 취소하길 원하시면 취소를 진행해주세요. "
                + "우천 시, 취소 패널티는 적용되지 않습니다.";

        WEATHER.message = message;
        return WEATHER;
    }
}
