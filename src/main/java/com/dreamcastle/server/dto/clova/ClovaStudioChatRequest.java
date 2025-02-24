package com.dreamcastle.server.dto.clova;

import java.util.ArrayList;

public record ClovaStudioChatRequest(
        ArrayList<Message> messages,
        double topP,
        double temperature,
        int maxTokens,
        double repeatPenalty
) {
}
