package com.example.RoscaApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RoscaCycle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "rosca_id", nullable = false)
    private Rosca rosca;

    @OneToOne
    @JoinColumn(name = "collector_id", nullable = false)
    private User collector;

    private int cycleNumber;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isActive;
}
