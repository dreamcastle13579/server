package com.dreamcastle.server.service.clova;

import com.dreamcastle.server.config.ClovaProperties;
import com.dreamcastle.server.dto.clova.ClovaStudioChatRequest;
import com.dreamcastle.server.dto.clova.ClovaStudioChatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ClovaStudioChatService {

    private final WebClient webClient;
    private final ClovaProperties clovaProperties;
    private final SystemPromptFactory systemPromptFactory;

    public Mono<ClovaStudioChatResponse> sendRequest(String promptType, String content) {

        SystemPromptType type = SystemPromptType.fromString(promptType);
        SystemPromptStrategy promptStrategy = systemPromptFactory.getStrategy(type);
        ClovaStudioChatRequest request = promptStrategy.createChatRequest(content);

        return webClient.post()
                .uri(clovaProperties.getStudioApiUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + clovaProperties.getApiKey())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ClovaStudioChatResponse.class);
    }
}
