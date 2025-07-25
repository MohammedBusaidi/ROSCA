package com.example.RoscaApp.controller;

import com.example.RoscaApp.dto.AuthenticationRequest;
import com.example.RoscaApp.dto.CreateUserRequest;
import com.example.RoscaApp.model.User;
import com.example.RoscaApp.security.ApplicationConfig;
import com.example.RoscaApp.security.JwtService;
import com.example.RoscaApp.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthenticationController {
    private final JwtService jwtService;
    private final ApplicationConfig applicationConfig;
    private final UserService service;
    @ResponseStatus(OK)
    @PostMapping("/token")
    public ResponseEntity authenticateUser(@Valid @RequestBody AuthenticationRequest req)
            throws Exception {
        User user;

        try {
            user = applicationConfig.authenticate(req.email(), req.password());
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
        var jwt = jwtService.generateToken(user.getId().toString(), user.getRole());
        service.authenticateToken(user);
        Map<String, String> response = Map.of(
                "accessToken", jwt,
                "userRole", user.getRole().name()
        );

        return ResponseEntity.status(OK)
                .body(response);


    }

    @ResponseStatus(CREATED)
    @PostMapping("/register")
    public UUID createUser(@Valid @RequestBody CreateUserRequest req) {
        UUID userId = service.createUser(req);
        return userId;
    }



}
