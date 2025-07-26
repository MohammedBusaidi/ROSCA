package com.example.RoscaApp.controller;

import com.example.RoscaApp.dto.CreateRoscaRequest;
import com.example.RoscaApp.service.RoscaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/rosca")
public class RoscaController {

    private final RoscaService roscaService;

    //Create Rosca
    @PostMapping
    public ResponseEntity<Map<String, UUID>> createRosca(
            @RequestParam UUID creatorId,
           @Valid @RequestBody CreateRoscaRequest request
            ) {
        UUID roscaId = roscaService.createRosca(creatorId, request);
        return ResponseEntity.ok(Map.of("roscaId", roscaId));
    }

    //Join Rosca
    @PostMapping("/{roscaId}/join")
    public ResponseEntity<Map<String, UUID>> joinRosca(
            @PathVariable UUID roscaId,
            @RequestParam UUID userId,
            @RequestParam(required = false) Integer pin
    ) {
        UUID joinedRoscaId = roscaService.joinRosca(roscaId, userId, pin);
        return ResponseEntity.ok(Map.of("roscaId", joinedRoscaId));
    }

    //Delete Rosca
    @DeleteMapping("/{roscaId}/leave")
    public ResponseEntity<Map<String, UUID>> leaveRosca(
            @PathVariable UUID roscaId,
            @RequestParam UUID userId
    ) {
        UUID resultId = roscaService.leaveRosca(roscaId, userId);
        return ResponseEntity.ok(Map.of("roscaId", resultId));
    }

    //Get all Roscas for user
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllRoscasByUser(
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(roscaService.getUserRoscas(userId));
    }
}
