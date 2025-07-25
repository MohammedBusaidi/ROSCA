package com.example.RoscaApp.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Contribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate datePaid;
    private boolean received;

    @ManyToOne
    private User contributor;

    @ManyToOne
    private RoscaGroup group;
}
