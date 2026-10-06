package com.health.app.service.impl;

import com.health.app.service.EmailNormalizationService;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class EmailNormalizationServiceImpl implements EmailNormalizationService {

    @Override
    public String normalize(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
