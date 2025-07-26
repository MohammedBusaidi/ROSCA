package com.example.RoscaApp.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record RoscaResponse(
        UUID id,
        String title,
        boolean isActive,
        int memberCount,
        LocalDateTime createdAt
) {
}
