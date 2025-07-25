package com.example.RoscaApp.exception;


/**
 * Exception for unauthorized access attempts
 */
public class UnverifiedUser extends AuthServiceException {
    public UnverifiedUser(String message) {
        super(message);
    }
}