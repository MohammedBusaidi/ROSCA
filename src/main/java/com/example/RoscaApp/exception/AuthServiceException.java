package com.example.RoscaApp.exception;

/**
 * Base exception class for authentication service
 */
public class AuthServiceException extends RuntimeException {
    public AuthServiceException(String message) {
        super(message);
    }

    public AuthServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
