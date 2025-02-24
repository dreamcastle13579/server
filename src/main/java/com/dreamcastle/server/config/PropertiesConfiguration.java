package com.dreamcastle.server.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {ClovaProperties.class, SystemPromptProperties.class})
public class PropertiesConfiguration {
}
