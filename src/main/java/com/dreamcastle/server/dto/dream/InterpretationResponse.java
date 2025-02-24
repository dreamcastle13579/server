package com.dreamcastle.server.dto.dream;

import java.util.List;

public record InterpretationResponse(List<String> messages, Category category) {
}
