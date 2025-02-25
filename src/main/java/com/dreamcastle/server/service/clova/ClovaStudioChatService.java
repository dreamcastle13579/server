package com.dreamcastle.server.service.clova;

import com.dreamcastle.server.config.ClovaProperties;
import com.dreamcastle.server.dto.clova.ClovaStudioChatRequest;
import com.dreamcastle.server.dto.clova.ClovaStudioChatResponse;
import com.dreamcastle.server.exception.ClovaApiException;
import com.dreamcastle.server.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
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

    public Mono<String> sendRequest(String promptType, String content) {

        SystemPromptType type = SystemPromptType.fromString(promptType);
        SystemPromptStrategy promptStrategy = systemPromptFactory.getStrategy(type);
        ClovaStudioChatRequest request = promptStrategy.createChatRequest(content);

        return webClient.post()
                .uri(clovaProperties.getStudioApiUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + clovaProperties.getApiKey())
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new ClovaApiException(ErrorCode.CLOVA_API_INVALID_REQUEST))
                )
                .bodyToMono(ClovaStudioChatResponse.class)
                .map(response -> response.result().message().content());
    }
}
