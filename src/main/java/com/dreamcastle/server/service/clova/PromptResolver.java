package com.dreamcastle.server.service.clova;

import com.dreamcastle.server.config.SystemPromptProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class PromptResolver {
    private final SystemPromptProperties properties;

    public String getSystemPromptMessage(SystemPromptType type) {
        String encodedPrompt = properties.getPrompts().get(type.getType());
        byte[] decodedBytes = Base64.getDecoder().decode(encodedPrompt);
        return new String(decodedBytes);
    }
}
