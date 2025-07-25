package com.example.RoscaApp.controller;

import com.example.RoscaApp.dto.CreateRoscaRequest;
import com.example.RoscaApp.service.RoscaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/rosca")
@RequiredArgsConstructor
public class RoscaController {

    private final RoscaService roscaService;

    @PostMapping
    public ResponseEntity<Map<String, UUID>> createRosca(
            @RequestParam UUID creatorId,
            @RequestBody CreateRoscaRequest request
            ) {
        UUID roscaId = roscaService.createRosca(creatorId, request);
        return ResponseEntity.ok(Map.of("roscaId", roscaId));
    }

    @PostMapping
    public ResponseEntity<Map<String, UUID>> joinRosca(
            @PathVariable UUID roscaId,
            @RequestParam UUID userId,
            @RequestParam(required = false) Integer pin
    ) {
        UUID joinedRoscaId = roscaService.joinRosca(roscaId, userId, pin);
        return ResponseEntity.ok(Map.of("roscaId", joinedRoscaId));
    }
}
