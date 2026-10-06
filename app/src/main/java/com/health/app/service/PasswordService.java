package com.health.app.service;

public interface PasswordService {

    String hash(String rawPassword);
    boolean matches(String rawPassword, String passwordHash);
}
