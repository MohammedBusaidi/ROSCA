package com.example.RoscaApp.dto;

public record AuthenticationRequest(
    String email,
    String password
) {
}
