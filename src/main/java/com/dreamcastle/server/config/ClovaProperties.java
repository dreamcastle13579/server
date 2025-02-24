package com.dreamcastle.server.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "clova")
@Getter
@RequiredArgsConstructor
public class ClovaProperties {
    private final String apiKey;
    private final String studioApiUrl;
}
