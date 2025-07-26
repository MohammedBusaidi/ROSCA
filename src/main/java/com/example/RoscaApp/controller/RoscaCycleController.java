package com.example.RoscaApp.controller;

import com.example.RoscaApp.service.RoscaCycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rosca-cycles")
public class RoscaCycleController {

    private final RoscaCycleService roscaCycleService;

    @PostMapping("/{roscaId}/start")
    public ResponseEntity<Map<String, UUID>> startCycle(@PathVariable UUID roscaId) {
        UUID cycleId = roscaCycleService.startNewCycle(roscaId);
        return ResponseEntity.ok(Map.of("cycleId", cycleId));
    }

    @PostMapping("/{cycleId}/close")
    public ResponseEntity<Map<String, UUID>> closeCycle(@PathVariable UUID cycleId) {
        UUID closedId = roscaCycleService.closeCycle(cycleId);
        return ResponseEntity.ok(Map.of("cycleId", closedId));
    }
}
