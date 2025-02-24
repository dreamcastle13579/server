package com.dreamcastle.server.dto.dream;

import jakarta.annotation.Nullable;

public record InterpretationRequest(@Nullable String promptType, String nickname, String content) {
}
