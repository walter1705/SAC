package com.uniquindio.backend.util.exception;

public class IAServiceUnavailableException extends RuntimeException {

    public IAServiceUnavailableException(String message) {
        super(message);
    }

    public IAServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
