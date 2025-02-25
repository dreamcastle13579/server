package com.dreamcastle.server.dto.dream;

import com.dreamcastle.server.exception.ClovaApiException;
import com.dreamcastle.server.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Category {
    JOY("기쁨"),
    HAPPINESS("행복"),
    SADNESS("슬픔"),
    ANGER("분노"),
    FEAR("두려움");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Category fromString(String value) {
        if (value == null || value.isEmpty()) {
            throw new ClovaApiException(ErrorCode.CLOVA_API_INVALID_RESPONSE_CATEGORY);
        }

        return Arrays.stream(Category.values())
                .filter(category -> category.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new ClovaApiException(ErrorCode.CLOVA_API_INVALID_RESPONSE_CATEGORY));
    }
}