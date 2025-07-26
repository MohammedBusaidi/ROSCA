package com.example.RoscaApp.service;

import com.example.RoscaApp.exception.InvalidInputException;
import com.example.RoscaApp.model.Rosca;
import com.example.RoscaApp.model.RoscaCycle;
import com.example.RoscaApp.model.User;
import com.example.RoscaApp.repository.RoscaCycleRepository;
import com.example.RoscaApp.repository.RoscaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoscaCycleService {

    private final RoscaCycleRepository roscaCycleRepository;
    private final RoscaRepository roscaRepository;

    public UUID startNewCycle(UUID roscaId) {

        Rosca rosca = roscaRepository.findById(roscaId)
                .orElseThrow(() -> {
                    log.warn("Rosca with ID {} not found", roscaId);
                    return new InvalidInputException("Rosca not found");
                });
        if (!rosca.isActive()) {
            log.warn("Rosca {} is not active. Cannot start a cycle.", roscaId);
            throw new InvalidInputException("Rosca is not active");
        }
        List<RoscaCycle> cycles = roscaCycleRepository.findByRosca(rosca);

        boolean anyActive = cycles.stream().anyMatch(RoscaCycle::isActive);

        if (anyActive) {
            log.warn("Rosca {} already has an active cycle", roscaId);
            throw new InvalidInputException("A cycle is already active");
        }
        Set<UUID> previousCollectors = cycles.stream()
                .map(c -> c.getCollector().getId())
                .collect(Collectors.toSet());

        List<User> eligibleCollector = rosca.getMembers().stream()
                .filter(u -> !previousCollectors.contains(u.getId()))
                .toList();

        if (eligibleCollector.isEmpty()) {
            log.warn("All members have already collected for Rosca {}", roscaId);
            throw new InvalidInputException("All members have already collected");
        }
        User collector = eligibleCollector.get(ThreadLocalRandom.current().nextInt(eligibleCollector.size()));
        log.info("Selected collector {}", collector.getId());

        RoscaCycle newCycle = RoscaCycle.builder()
                .rosca(rosca)
                .collector(collector)
                .cycleNumber(cycles.size() + 1)
                .startDate(LocalDateTime.now())
                .isActive(true)
                .build();

        RoscaCycle roscaCycleSaved = roscaCycleRepository.save(newCycle);
        log.info("Cycle {} started successfully", roscaCycleSaved.getCycleNumber());

        return roscaCycleSaved.getId();
    }

    public UUID closeCycle(UUID cycleId) {

        RoscaCycle cycle = roscaCycleRepository.findById(cycleId)
                .orElseThrow(() -> {
                    log.warn("Cycle with ID {} not found", cycleId);
                    return new InvalidInputException("Cycle not found");
                });
        if (!cycle.isActive()) {
            log.warn("Cycle {} is already closed",cycleId);
            throw new InvalidInputException("Cycle is already closed");
        }
        cycle.setActive(false);
        cycle.setEndDate(LocalDateTime.now());
        roscaCycleRepository.save(cycle);

        log.info("Cycle {} successfully closed", cycleId);
        return cycle.getId();
    }
}
