package com.health.app.service.impl;

import com.health.app.service.PasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordServiceImpl implements PasswordService {

    private final PasswordEncoder passwordEncoder;

    public PasswordServiceImpl(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String hash(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String passwordHash){
        return passwordEncoder.matches(rawPassword, passwordHash);
    }
}
