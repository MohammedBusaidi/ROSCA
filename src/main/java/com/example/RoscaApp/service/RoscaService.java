package com.example.RoscaApp.service;

import com.example.RoscaApp.dto.CreateRoscaRequest;
import com.example.RoscaApp.dto.RoscaResponse;
import com.example.RoscaApp.exception.InvalidInputException;
import com.example.RoscaApp.model.Rosca;
import com.example.RoscaApp.model.User;
import com.example.RoscaApp.repository.RoscaRepository;
import com.example.RoscaApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoscaService {
    private final RoscaRepository roscaRepository;
    private final UserRepository userRepository;

    //create a rosca
    public UUID createRosca(UUID creatorId, CreateRoscaRequest request) {
        log.warn("Attempting to create Rosca by user: {}", creatorId);

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> {
                    log.warn("Creator with ID {} not found", creatorId);
                    return new InvalidInputException("Creator not found");
                });

        //check title input
        if (request.title() == null || request.title().isBlank()) {
            log.warn("Title is missing");
            throw new InvalidInputException("Title cannot be empty");
        }

        //check amount
        if (request.contributionAmount() <= 0) {
            log.warn("Invalid Contribution amount from user {}: {}", creatorId, request.contributionAmount());
            throw new InvalidInputException(("Contribution amount must be more than 0"));
        }

        if (request.codePin() != null && (request.codePin() < 1000 || request.codePin() > 9999)) {
            throw new InvalidInputException("PIN must be a 4-digit number.");
        }

        Rosca rosca = Rosca.builder()
                .id(UUID.randomUUID())
                .creator(creator)
                .title(request.title())
                .contributionAmount(request.contributionAmount())
                .codePin(request.codePin())
                .createdAt(LocalDateTime.now())
                .active(false)
                .members(new HashSet<>(Set.of(creator)))
                .build();

        Rosca savedRosca = roscaRepository.save(rosca);
        log.info("Rosca created successfully with ID: {}", savedRosca.getId());

        return savedRosca.getId();
    }

    //join a rosca
    public UUID joinRosca(UUID roscaId, UUID userId, Integer pin) {
        log.info("User {} attempting to join Rosca {}", userId, roscaId);

        Rosca rosca = roscaRepository.findById(roscaId)
                .orElseThrow(() -> {
                    log.warn("Rosca with ID {} not found", roscaId);
                    return new InvalidInputException("Rosca not found");
                });

        //check if rosca is active or not
        if (rosca.isActive()) {
            log.warn("User {} tried to join active Rosca {}", userId, roscaId);
            throw new InvalidInputException("Rosca is already active.");
        }

        //pin validation
        if (rosca.getCodePin() != null && !rosca.getCodePin().equals(pin)) {
            log.warn("Incorrect PIN provided by user {}", userId);
            throw new InvalidInputException("Invalid PIN.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User with ID {} not found", userId);
                    return new InvalidInputException("User not found");
                });

        //check if user already joined or not
        if (rosca.getMembers().contains(user)) {
            log.warn("User {} is already a member of Rosca {}", userId, roscaId);
            throw new InvalidInputException("User already joined.");
        }

        rosca.getMembers().add(user);
        Rosca savedRosca = roscaRepository.save(rosca);

        log.info("User {} successfully joined Rosca {}", userId, savedRosca.getId());
        return savedRosca.getId();
    }

    //get all roscas that user have
    public List<RoscaResponse> getUserRoscas(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User with ID {} not found", userId);
                    return new InvalidInputException("User not found");
                });

        List<Rosca> roscas = roscaRepository.findAllByMembersContaining(user);

        return roscas.stream()
                .map(rosca -> new RoscaResponse(
                        rosca.getId(),
                        rosca.getTitle(),
                        rosca.isActive(),
                        rosca.getMembers().size(),
                        rosca.getCreatedAt()
                ))
                .toList();
    }

    public UUID leaveRosca(UUID roscaId, UUID userId) {
        Rosca rosca = roscaRepository.findById(roscaId)
                .orElseThrow(() -> new InvalidInputException("Rosca not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidInputException("User not found"));

        if (!rosca.getMembers().contains(user)) {
            log.warn("User {} is not a member of Rosca {}", userId, roscaId);
            throw new InvalidInputException("User is not a member of this Rosca");
        }
        if (rosca.isActive()) {
            log.warn("User {} tried to leave an active Rosca {}", userId, roscaId);
            throw new InvalidInputException("Cannot leave an active Rosca");
        }
        rosca.getMembers().remove(user);
        roscaRepository.save(rosca);

        log.info("User {} successfully left Rosca {}", userId, roscaId);
        return rosca.getId();
    }
}
