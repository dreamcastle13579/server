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
    LOVE_RELATIONSHIP("사랑&관계"),
    ACHIEVEMENT_CHALLENGE("성취&도전"),
    WEALTH_OPPORTUNITY("재물&기회"),
    HEALTH_RECOVERY("건강&회복"),
    FEAR_OVERCOMING("두려움&극복"),
    LUCK_POSITIVE_CHANGE("행운&긍정적변화");

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
        String normalizedValue = value.trim().replaceAll(" ", "");

        return Arrays.stream(Category.values())
                .filter(category -> category.value.equals(normalizedValue))
                .findFirst()
                .orElseThrow(() -> new ClovaApiException(ErrorCode.CLOVA_API_INVALID_RESPONSE_CATEGORY));
    }
}