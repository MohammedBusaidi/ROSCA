package com.example.RoscaApp.repository;

import com.example.RoscaApp.model.Rosca;
import com.example.RoscaApp.model.RoscaCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoscaCycleRepository extends JpaRepository<RoscaCycle, UUID> {
    List<RoscaCycle> findByRosca(Rosca rosca);
}
