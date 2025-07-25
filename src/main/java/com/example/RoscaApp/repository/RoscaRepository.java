package com.example.RoscaApp.repository;

import com.example.RoscaApp.model.Rosca;
import com.example.RoscaApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoscaRepository extends JpaRepository<Rosca, UUID> {
    List<Rosca> findAllByMembersContaining(User user); // to fetch all roscas a user has joined


}
