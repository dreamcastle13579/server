package com.dreamcastle.server.exception;

public class ClovaApiException extends ServerException {
    public ClovaApiException(ErrorCode errorCode) {
        super(errorCode);
    }
}
