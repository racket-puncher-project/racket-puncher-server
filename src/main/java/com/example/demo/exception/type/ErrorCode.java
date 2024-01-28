package com.example.demo.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "주소가 존재하지 않습니다."),
    APPLY_ALREADY_CANCELED(HttpStatus.BAD_REQUEST.value(), "해당 매칭에 대한 참가 신청은 이미 취소되었습니다."),
    MATCHING_ALREADY_FINISHED(HttpStatus.BAD_REQUEST.value(), "이미 매칭이 종료된 경기입니다."),
    MATCHING_ALREADY_CONFIRMED(HttpStatus.BAD_REQUEST.value(), "이미 매칭이 확정된 경기입니다."),
    APPLY_ALREADY_EXISTED(HttpStatus.BAD_REQUEST.value(), "이미 참가 신청한 경기입니다."),
    APPLY_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "참가 신청 내역이 없습니다."),
    EMAIL_ALREADY_EXISTED(HttpStatus.BAD_REQUEST.value(), "이미 사용 중인 이메일입니다."),
    NICKNAME_ALREADY_EXISTED(HttpStatus.BAD_REQUEST.value(), "이미 사용 중인 닉네임입니다."),
    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND.value(),"이메일을 찾을 수 없습니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다."),
    APPLY_CANCEL_FAILED(HttpStatus.BAD_REQUEST.value(), "참가 신청 취소에 실패하였습니다."),
    APPLY_FAILED(HttpStatus.BAD_REQUEST.value(), "참가 신청에 실패하였습니다."),
    JSON_PARSING_FAILED(HttpStatus.BAD_REQUEST.value(), "JsonParse 과정에서 예외가 발생하였습니다."),
    MATCHING_NOT_FOUND(HttpStatus.NOT_FOUND.value(),"해당 매칭을 찾을 수 없습니다."),
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED.value(), "로그인이 필요한 서비스입니다."),
    PERMISSION_DENIED_TO_EDIT_AND_DELETE_MATCHING(HttpStatus.BAD_REQUEST.value(), "매칭을 수정 및 삭제할 권한이 없습니다."),
    PERMISSION_DENIED_TO_ACCEPTED_APPLIES(HttpStatus.BAD_REQUEST.value(), "참가 신청을 수락할 권한이 없습니다."),
    NOTIFICATION_CONNECTION_FAILED(HttpStatus.BAD_REQUEST.value(), "알림 연결에 실패하였습니다."),
    RECRUIT_NUMBER_OVERED(HttpStatus.BAD_REQUEST.value(), "모집 인원보다 많은 인원을 수락할 수 없습니다."),
    S3_UPLOAD_FAILED(HttpStatus.BAD_REQUEST.value(), "파일 업로드가 실패했습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "엑세스 토큰이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 엑세스 토큰입니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "리프레시 토큰이 만료되었습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(),"사용자를 찾을 수 없습니다."),
    SELF_APPLY_CANCEL_DENIED(HttpStatus.BAD_REQUEST.value(), "본인이 주최한 경기는 참가 신청 취소를 할 수 없습니다."),
    KAKAO_ACCESS_TOKEN_FAIL(HttpStatus.BAD_REQUEST.value(),"카카오 엑세스 토큰 발급에 실패했습니다."),
    KAKAO_USER_INFO_FAIL(HttpStatus.BAD_REQUEST.value(),"카카오 사용자 정보 조회에 실패했습니다."),
    SMS_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR.value(), "sms 문자 전송에 실패했습니다."),
    PHONE_AUTH_NUM_EXPIRED(HttpStatus.BAD_REQUEST.value(), "휴대폰 인증 번호가 만료되었습니다."),
    PHONE_AUTH_NUM_DOESNT_MATCH(HttpStatus.BAD_REQUEST.value(), "휴대폰 인증 번호가 일치하지 않습니다."),
    LAT_AND_LON_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "위경도를 찾을 수 없는 주소입니다."),
    REGISTRATION_INFO_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "가입 정보가 없습니다."),
    RESET_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "리셋 토큰이 만료되었습니다."),
    RESET_TOKEN_ALREADY_USED(HttpStatus.UNAUTHORIZED.value(), "이미 사용된 리셋 토큰입니다.")
    ;

    private final int code;
    private final String description;
}