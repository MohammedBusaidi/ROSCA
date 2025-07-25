package com.example.RoscaApp.service;

import com.example.RoscaApp.dto.CreateRoscaRequest;
import com.example.RoscaApp.dto.RoscaDTO;
import com.example.RoscaApp.exception.InvalidInputException;
import com.example.RoscaApp.model.Rosca;
import com.example.RoscaApp.model.User;
import com.example.RoscaApp.repository.RoscaRepository;
import com.example.RoscaApp.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RoscaService {
    private final RoscaRepository roscaRepository;
    private final UserRepository userRepository;


    public UUID createRosca(UUID creatorId, CreateRoscaRequest request) {
        User creator = userRepository.findById(creatorId).orElseThrow(() -> new InvalidInputException("Creator not found"));

        //check title input
        if (request.title() == null || request.title().isBlank()) {
            throw new InvalidInputException("Title cannot be empty");
        }

        //check amount
        if (request.contributionAmount() <=0) {
            throw new InvalidInputException(("Contribution amount must be more the 0"));
        }

        Rosca rosca = Rosca.builder()
                .id(UUID.randomUUID())
                .creator(creator)
                .title(request.title())
                .contributionAmount(request.contributionAmount())
                .codePin(request.codePin())
                .createdAt(LocalDateTime.now())
                .isActive(false)
                .members(new HashSet<>(Set.of(creator)))
                .build();

        Rosca savedRosca = roscaRepository.save(rosca);

        return savedRosca.getId();
    }

    public UUID joinRosca(UUID roscaId, UUID userId, Integer pin) {
        Rosca rosca = roscaRepository.findById(roscaId)
                .orElseThrow(() -> new InvalidInputException("Rosca not found"));

        //check if rosca is active or not
        if (rosca.isActive()) {
            throw new InvalidInputException("Rosca is already active.");
        }

        //pin validation
        if (rosca.getCodePin() != null && !rosca.getCodePin().equals(pin)) {
            throw new InvalidInputException("Invalid PIN.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidInputException("User not found"));

        //check if user already joined or not
        if (rosca.getMembers().contains(user)) {
            throw new InvalidInputException("User already joined.");
        }

        rosca.getMembers().add(user);
        Rosca savedRosca = roscaRepository.save(rosca);

        return savedRosca.getId();
    }
}
