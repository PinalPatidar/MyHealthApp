package com.health.app.service;

public interface EmailVerificationTokenService {

    String generateRawToken();
    String hashToken(String rawToken);

}