package com.dreamcastle.server.dto.clova;

import java.util.List;

public record ClovaStudioChatRequest(
        List<Message> messages,
        double topP,
        double temperature,
        int maxTokens,
        double repeatPenalty
) {
}
