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
public class RoscaGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double contributionAmount;
    private int rotationDays;
    private LocalDate startDate;

    @ManyToMany
    private List<User> members;

    @OneToOne
    private User currentReceiver;
}
