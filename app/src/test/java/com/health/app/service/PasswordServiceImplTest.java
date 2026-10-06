package com.health.app.service;

import com.health.app.service.impl.PasswordServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordServiceImplTest {

    private final PasswordService passwordService = new PasswordServiceImpl(Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());

    @Test
    void shouldHashPassword(){
        String rawPassword = "StrongPassword@123";
        String hash = passwordService.hash(rawPassword);
        assertNotNull(hash);
        assertNotEquals(rawPassword, hash);
    }

    @Test
    void shouldMatchCorrectPassword(){
        String rawPassword = "StrongPassword@123";
        String hash = passwordService.hash(rawPassword);
        assertTrue(passwordService.matches(rawPassword, hash));
    }

    @Test
    void shouldRejectIncorrectPassword(){
        String hash = passwordService.hash("StrongPassword@123");
        assertFalse(passwordService.matches("wrongPassword@123", hash));
    }

    @Test
    void shouldGenerateDifferentHashesForSamePassword(){
        String rawPassword = "StrongPassword@123";
        String firstHash = passwordService.hash(rawPassword);
        String secondHash = passwordService.hash(rawPassword);

        assertNotEquals(firstHash, secondHash);
        assertTrue(passwordService.matches(rawPassword,firstHash));
        assertTrue(passwordService.matches(rawPassword,secondHash));
    }
}
