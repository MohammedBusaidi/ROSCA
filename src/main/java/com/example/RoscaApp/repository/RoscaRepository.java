package com.example.RoscaApp.repository;

import com.example.RoscaApp.model.Rosca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoscaRepository extends JpaRepository<Rosca, UUID> {
}
