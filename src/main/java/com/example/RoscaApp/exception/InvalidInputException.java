package com.example.RoscaApp.exception;


/**
 * Exception for invalid user input
 */
public class InvalidInputException extends AuthServiceException {
    public InvalidInputException(String message) {
        super(message);
    }
}