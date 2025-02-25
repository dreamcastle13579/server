package com.dreamcastle.server.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "prompt.system")
@Getter
@RequiredArgsConstructor
public class SystemPromptProperties {
    private final Map<String, String> prompts = new HashMap<>();
}
