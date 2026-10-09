package com.health.app.service.impl;

import com.health.app.service.EmailVerificationTokenService;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class EmailVerificationTokenServiceImpl implements EmailVerificationTokenService {

    private static final int TOKEN_BYTES = 32;
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String generateRawToken() {
        byte[] tokenBytes = new byte[TOKEN_BYTES];
        secureRandom.nextBytes(tokenBytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(tokenBytes);
    }

    @Override
    public String hashToken(String rawToken) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(
                rawToken.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        return java.util.HexFormat.of().formatHex(hashBytes);
        } catch(NoSuchAlgorithmException exception){
            throw new IllegalStateException(
                    "SHA-256 algorithm is unavailable", exception);
        }
    }
}
