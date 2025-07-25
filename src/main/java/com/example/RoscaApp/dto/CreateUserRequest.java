package com.example.RoscaApp.dto;

public record CreateUserRequest(
    String name,
    String email,
    String password,
    Role role
) {
}
