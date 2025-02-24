package com.dreamcastle.server.service.dream;

import com.dreamcastle.server.dto.clova.ClovaStudioChatResponse;
import com.dreamcastle.server.dto.dream.InterpretationResponse;
import com.dreamcastle.server.service.clova.ClovaStudioChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class DreamService {
    private final ClovaStudioChatService clovaStudioChatService;

    public Mono<InterpretationResponse> interpret(String promptType, String nickname, String dreamContent) {
        String content = formatDreamContent(nickname, dreamContent);
        return clovaStudioChatService.sendRequest(promptType, content)
                .timeout(Duration.ofSeconds(10)) // ⏳ 10초 제한 적용
                .map(this::parseResponse)
                .onErrorMap(TimeoutException.class, e ->
                        new RuntimeException("Clova API timeout exceeded", e));
    }

    private String formatDreamContent(String nickname, String content) {
        return String.format("%s: %s", nickname, content);
    }

    private InterpretationResponse parseResponse(ClovaStudioChatResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(
                    response.result().message().content(), InterpretationResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Clova response", e);
        }
    }
}
