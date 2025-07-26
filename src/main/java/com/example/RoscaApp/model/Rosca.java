package com.example.RoscaApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Rosca {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToMany
    @JoinTable(name = "rosca_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private Set<User> members;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int contributionAmount;

    @Column
    private Integer codePin;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime expireAt;

    @Column(nullable = false)
    private boolean active;
}
