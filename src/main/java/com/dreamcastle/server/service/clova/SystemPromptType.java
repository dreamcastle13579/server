package com.dreamcastle.server.service.clova;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum SystemPromptType {
    FAIRY("fairy");

    private final String type;
    public static SystemPromptType fromString(String value) {
        return Arrays.stream(values())
                .filter(promptType -> promptType.type.equalsIgnoreCase(value))
                .findFirst()
                .orElse(FAIRY);
    }
}