package com.dreamcastle.server.service.clova;

import com.dreamcastle.server.config.ClovaProperties;
import com.dreamcastle.server.config.SystemPromptProperties;
import com.dreamcastle.server.dto.clova.ClovaStudioChatRequest;
import com.dreamcastle.server.dto.clova.ClovaStudioChatResponse;
import com.dreamcastle.server.dto.clova.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ClovaStudioChatService {

    private final WebClient webClient;
    private final SystemPromptProperties properties;
    private final ClovaProperties clovaProperties;

    public Mono<ClovaStudioChatResponse> sendRequest(String promptType, String content) {

        ClovaStudioChatRequest request = createChatRequest(promptType, content);

        return webClient.post()
                .uri(clovaProperties.getStudioApiUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + clovaProperties.getApiKey())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ClovaStudioChatResponse.class);
    }


    private ClovaStudioChatRequest createChatRequest(String promptType, String content) {
        SystemPromptType type = SystemPromptType.fromString(promptType);
        String systemPrompt = properties.getDecodedPrompt(type);
        ArrayList<Message> messages = new ArrayList<>();
        messages.add(Message.createSystemMessage(systemPrompt));
        messages.add(Message.createUserMessage(content));
        return new ClovaStudioChatRequest(messages, 0.8, 0.5, 500, 5.0);
    }
}
