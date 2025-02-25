package com.dreamcastle.server.dto.dream;

import com.dreamcastle.server.exception.ClovaApiException;
import com.dreamcastle.server.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public record InterpretationResponse(List<String> messages, Category category) {
    public InterpretationResponse {
        if (messages == null || messages.size() != 6) {
            throw new ClovaApiException(ErrorCode.CLOVA_API_INVALID_RESPONSE_MESSAGE);
        }
        if (category == null) {
            throw new ClovaApiException(ErrorCode.CLOVA_API_INVALID_RESPONSE_CATEGORY);
        }
    }

    public static InterpretationResponse parseResponse(String clovaResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(clovaResponse, InterpretationResponse.class);
        } catch (JsonProcessingException ex) {
            if (ex.getCause() instanceof ClovaApiException clovaApiException) {
                throw clovaApiException;
            }
            throw new ClovaApiException(ErrorCode.CLOVA_API_RESPONSE_PARSING_ERROR);
        }
    }
}
