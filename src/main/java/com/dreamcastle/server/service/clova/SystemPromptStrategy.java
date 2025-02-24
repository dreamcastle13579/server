package com.dreamcastle.server.service.clova;

import com.dreamcastle.server.dto.clova.ClovaStudioChatRequest;

public interface SystemPromptStrategy {
    SystemPromptType getType();
    ClovaStudioChatRequest createChatRequest(String content);
}
