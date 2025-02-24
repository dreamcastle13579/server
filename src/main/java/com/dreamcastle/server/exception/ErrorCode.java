package com.dreamcastle.server.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // CLOVA 관련
    CLOVA_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Clova API 요청 중 오류가 발생했습니다."),
    CLOVA_API_TIME_OUT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Clova API 요청시간이 초과되었습니다.."),

    // SERVER
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
    private final HttpStatus status;
    private final String message;
}
