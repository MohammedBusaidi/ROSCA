package com.example.RoscaApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class RoscaCycle {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "rosca_id", nullable = false)
    private Rosca rosca;

    @OneToOne
    @JoinColumn(name = "collector_id", nullable = false, unique = true)
    private User collector;

    @Column(nullable = false)
    private int cycleNumber;

    @Column(nullable = false)
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Column(nullable = false)
    private boolean isActive;
}
