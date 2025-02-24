package com.dreamcastle.server.service.clova;

import com.dreamcastle.server.dto.clova.ClovaStudioChatRequest;
import com.dreamcastle.server.dto.clova.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FairySystemPrompt implements SystemPromptStrategy {

    private final PromptResolver promptResolver;

    @Override
    public SystemPromptType getType() {
        return SystemPromptType.FAIRY;
    }

    @Override
    public ClovaStudioChatRequest createChatRequest(String content) {
        String systemPrompt = promptResolver.getSystemPromptMessage(getType());
        List<Message> messages = List.of(
                Message.createSystemMessage(systemPrompt),
                Message.createUserMessage(content)
        );
        return new ClovaStudioChatRequest(messages, 0.8, 0.5, 500, 5.0);
    }
}
