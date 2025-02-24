package com.dreamcastle.server.service.clova;

import com.dreamcastle.server.exception.ClovaApiException;
import com.dreamcastle.server.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SystemPromptFactory {

    private final List<SystemPromptStrategy> strategies;

    public SystemPromptStrategy getStrategy(SystemPromptType type) {
        return strategies.stream()
                .filter(strategy -> strategy.getType() == type)
                .findFirst()
                .orElseThrow(() -> new ClovaApiException(ErrorCode.INVALID_PROMPT_TYPE));
    }
}