package com.example.RoscaApp.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateRoscaRequest(
        @NotBlank String title,
        @Min(1) int contributionAmount,
        @Min(1000) @Max(9999) Integer codePin
) {
}
