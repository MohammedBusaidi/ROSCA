package com.example.RoscaApp.security;
import com.example.RoscaApp.model.User;
import com.example.RoscaApp.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Service
@AllArgsConstructor
@Slf4j
public class ApplicationConfig {

    private final UserService userService;

    public User loadUserByUsername(String email) {
        return userService.loadUserByEmail(email);
    }

    public User authenticate(String email, String password) throws NoSuchAlgorithmException {

        if (email.isEmpty() || password.isEmpty()) {
            throw new BadCredentialsException("Unauthorized");
        }

        var userEntity = loadUserByUsername(email);

        if (userEntity == null) {
            throw new BadCredentialsException("Unauthorized");
        }
        var verified = verifyPasswordHash(
                password,
                userEntity.getStoredHash(),
                userEntity.getStoredSalt()
        );

        if (!verified) {
            throw new BadCredentialsException("Unauthorized");
        }

        return userEntity;
    }

    private Boolean verifyPasswordHash(String password, byte[] storedHash, byte[] storedSalt)
            throws NoSuchAlgorithmException {

        if (password.isBlank() || password.isEmpty())
            throw new IllegalArgumentException("Password cannot be empty or whitespace only string.");

        if (storedHash.length != 64) {
            throw new IllegalArgumentException("Invalid length of password hash (64 bytes expected)");
        }

        if (storedSalt.length != 128) {
            throw new IllegalArgumentException("Invalid length of password salt (64 bytes expected).");
        }

        var md = MessageDigest.getInstance("SHA-512");
        md.update(storedSalt);

        var computedHash = md.digest(password.getBytes(StandardCharsets.UTF_8));

        for (int i = 0; i < computedHash.length; i++) {
            if (computedHash[i] != storedHash[i]) return false;
        }

        return MessageDigest.isEqual(computedHash, storedHash);
    }
}