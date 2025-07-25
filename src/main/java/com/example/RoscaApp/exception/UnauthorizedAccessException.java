package com.example.RoscaApp.exception;


/**
 * Exception for unauthorized access attempts
 */
public class UnauthorizedAccessException extends AuthServiceException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}