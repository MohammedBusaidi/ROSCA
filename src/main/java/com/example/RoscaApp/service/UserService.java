package com.example.RoscaApp.service;

import com.example.RoscaApp.dto.CreateUserRequest;
import com.example.RoscaApp.exception.AuthServiceException;
import com.example.RoscaApp.exception.InvalidInputException;
import com.example.RoscaApp.exception.UnauthorizedAccessException;
import com.example.RoscaApp.exception.UserAlreadyExistsException;
import com.example.RoscaApp.model.User;
import com.example.RoscaApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;


    public User loadUserByEmail (String email){
        return userRepository.findByEmail(email).orElseThrow();
    }

    public UUID createUser(CreateUserRequest record) {
        try {
            // Validate input
            if (record.email() == null || record.email().isEmpty()) {
                throw new InvalidInputException("Email cannot be empty");
            }

            if (record.role() == null) {
                throw new InvalidInputException("Role must be specified");
            }

            // Check if user already exists
            Optional<User> existingUser = userRepository.findByEmail(record.email());
            if (existingUser.isPresent()) {
                log.warn("User already exists with email: {}", record.email());
                throw new
                        UserAlreadyExistsException(record.email());
            }
            byte[] salt = createSalt();
            byte[] hashedPassword = createPasswordHash(record.password(), salt);

            // Create and save the user
            User user = User.builder()
                    .email(record.email())
                    .storedHash(hashedPassword)
                    .storedSalt(salt)
                    .name(record.name())
                    .role(record.role())
                    .build();

            User savedUser = userRepository.save(user);
            log.info("User created successfully with ID: {}", savedUser.getId());
            return savedUser.getId();
        } catch (Exception e) {
            if (e instanceof AuthServiceException) {
                // Re-throw our custom exceptions
                throw e;
            }
            log.error("Error creating user: {}", e.getMessage(), e);
            throw new AuthServiceException("Failed to create user: " + e.getMessage(), e);
        }
    }


    public void authenticateToken(User applicationUser) {
        try {
            if (applicationUser == null) {
                throw new InvalidInputException("User cannot be null");
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        applicationUser,
                        null,
                        applicationUser.getAuthorities()
                );
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(usernamePasswordAuthenticationToken);

                log.debug("User authenticated successfully: {}", applicationUser.getEmail());
            }
        } catch (Exception e) {
            if (e instanceof AuthServiceException) {
                // Re-throw our custom exceptions
                throw e;
            }
            log.error("Error authenticating user: {}", e.getMessage(), e);
            throw new UnauthorizedAccessException("Authentication failed: " + e.getMessage());
        }
    }

    private byte[] createSalt() {
        var random = new SecureRandom();
        var salt = new byte[128];
        random.nextBytes(salt);
        return salt;
    }

    private byte[] createPasswordHash(String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            return md.digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            log.error("Error creating password hash: {}", e.getMessage(), e);
            throw new AuthServiceException("Failed to hash password: " + e.getMessage(), e);
        }
    }
}
