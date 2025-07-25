package com.example.RoscaApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoscaDTO {
    private UUID id;
    private String title;
    private int contributionAmount;
    private Integer codePin;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime expireAt;

    private UserDTO creator;
    private List<UserDTO> members;
}
