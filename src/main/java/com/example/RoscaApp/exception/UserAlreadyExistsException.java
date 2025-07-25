package com.example.RoscaApp.exception;
//

/**
 * Exception for user already exists scenarios
 */
public class UserAlreadyExistsException extends AuthServiceException {
    public UserAlreadyExistsException(String email) {
        super("User already exists with email: " + email);
    }
}