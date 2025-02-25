package com.dreamcastle.server.dto;

import com.dreamcastle.server.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        Integer code,
        String message,
        T result
) {
    public static <T> ApiResponse<T> success(T result) {
        return new ApiResponse<>(200, "API 요청이 성공했습니다.", result);
    }

    public static <T> ApiResponse<T> error(T errorMessage) {
        return new ApiResponse<>(500, "예기치 않은 오류가 발생했습니다.", errorMessage);
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return new ApiResponse<>(errorCode.getStatus().value(), errorCode.getMessage(), null);
    }
}

