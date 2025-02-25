package com.dreamcastle.server.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // CLOVA 관련
    CLOVA_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Clova API 요청 중 오류가 발생했습니다."),
    CLOVA_API_TIME_OUT_ERROR(HttpStatus.GATEWAY_TIMEOUT, "Clova API 요청시간이 초과되었습니다."),
    CLOVA_API_RESPONSE_PARSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Clova API 응답을 파싱하는 중 오류가 발생했습니다."),
    CLOVA_API_INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Clova API에 잘못된 요청이 전달되었습니다."),
    CLOVA_API_INVALID_RESPONSE_MESSAGE(HttpStatus.INTERNAL_SERVER_ERROR, "Clova API 응답 메시지 형식 또는 개수가 올바르지 않습니다. (6개 필요)"),
    CLOVA_API_INVALID_RESPONSE_CATEGORY(HttpStatus.INTERNAL_SERVER_ERROR, "Clova API 응답의 category 값이 잘못된 형식입니다."),

    // SYSTEM PROMPT 관련
    INVALID_PROMPT_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 프롬프트 타입입니다."),

    // SERVER
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;
}
