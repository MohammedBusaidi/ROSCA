package com.example.RoscaApp.exception;


/**
 * Exception for invalid or malformed authentication token
 */
public class InvalidAuthTokenException extends AuthServiceException {
    public InvalidAuthTokenException(String message) {
        super(message);
    }

    public InvalidAuthTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}