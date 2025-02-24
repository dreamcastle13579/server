package com.dreamcastle.server.dto.clova;

public record Message(Role role, String content) {
    public static Message createSystemMessage(String prompt) {
        return new Message(Role.system, prompt);
    }

    public static Message createUserMessage(String content) {
        return new Message(Role.user, content);
    }
}
