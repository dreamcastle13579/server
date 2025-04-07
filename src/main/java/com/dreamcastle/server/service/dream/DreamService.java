package com.dreamcastle.server.service.dream;

import com.dreamcastle.server.dto.dream.InterpretationResponse;
import com.dreamcastle.server.exception.ClovaApiException;
import com.dreamcastle.server.exception.ErrorCode;
import com.dreamcastle.server.service.clova.ClovaStudioChatService;
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
                .timeout(Duration.ofSeconds(15)) // ⏳ 15초 제한 적용
                .map(InterpretationResponse::parseResponse)
                .onErrorMap(TimeoutException.class, e ->
                        new ClovaApiException(ErrorCode.CLOVA_API_TIME_OUT_ERROR));
    }

    private String formatDreamContent(String nickname, String content) {
        return String.format("%s: %s", nickname, content);
    }
}
