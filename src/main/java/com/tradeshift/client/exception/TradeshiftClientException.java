package com.tradeshift.client.exception;

public class TradeshiftClientException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public TradeshiftClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public TradeshiftClientException(String message) {
        super(message);
    }

    public TradeshiftClientException(Throwable cause) {
        super(cause);
    }
}
