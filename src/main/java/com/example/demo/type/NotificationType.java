package com.example.demo.type;

import com.example.demo.openfeign.dto.weather.WeatherResponseDto;

public enum NotificationType {
    MODIFY_MATCHING("신청한 매칭글이 수정돼, 매칭 확정 상태가 매칭 대기 상태로 변경되었습니다."),
    DELETE_MATCHING("신청한 매칭글이 삭제되었습니다."),
    REQUEST_APPLY("주최한 매칭에 참가 신청 요청이 들어왔습니다."),
    ACCEPT_APPLY("신청한 매칭글의 주최자가 참가 신청을 수락하였습니다."),
    CANCEL_APPLY("주최한 매칭에 참가 신청을 한 회원이 참가 취소를 하였습니다."),
    WEATHER("날씨 데이터에 맞는 메시지를 생성해주세요."),
    MATCHING_CLOSED("매칭에 성공하였습니다. 채팅방이 활성화됩니다."),
    MATCHING_FAILED("매칭에 실패하였습니다."),
    MATCHING_FINISHED("매칭이 종료되었습니다. 리뷰를 진행해주세요.");

    private String message;


    NotificationType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public static NotificationType makeWeatherIssueMessage(WeatherResponseDto weatherResponseDto) {
        String message = "오늘 날씨가 좋지 않을 것으로 예상됩니다. 예상되는 날씨 상태: " + weatherResponseDto.getPrecipitationType().getMessage()
                + "\n강수 확률은 " + weatherResponseDto.getPrecipitationProbability() + "입니다."
                + "\n오늘 진행 예정인 경기를 취소하길 원하시면 취소를 진행해주세요. "
                + "\n우천 시, 취소 패널티는 적용되지 않습니다.";

        WEATHER.message = message;
        return WEATHER;
    }

    public static NotificationType makeWeatherMessage() {
        String message = "오늘 날씨는 맑을 것으로 예상됩니다. 즐거운 경기 되세요:)";

        WEATHER.message = message;
        return WEATHER;
    }
}
