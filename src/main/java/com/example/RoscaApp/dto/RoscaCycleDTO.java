package com.example.RoscaApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoscaCycleDTO {
    private UUID id;
    private UUID roscaId;
    private int cycleNumber;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isActive;

    private UserDTO collector;
}
