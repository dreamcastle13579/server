package com.dreamcastle.server.config;

import com.dreamcastle.server.service.clova.SystemPromptType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Base64;
import java.util.Map;

@ConfigurationProperties(prefix = "prompt.system")
@Getter
@RequiredArgsConstructor
public class SystemPromptProperties {
    private final Map<String, String> prompts;

    public String getDecodedPrompt(SystemPromptType type) {
        String encodedPrompt = prompts.getOrDefault(type.getType(), "");
        byte[] decodedBytes = Base64.getDecoder().decode(encodedPrompt);
        return new String(decodedBytes);
    }
}
