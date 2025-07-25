package com.example.RoscaApp.dto;



public record CreateRoscaRequest(
        String title,
        int contributionAmount,
        Integer codePin
) {
}
