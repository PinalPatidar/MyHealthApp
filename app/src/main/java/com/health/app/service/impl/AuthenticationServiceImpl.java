package com.health.app.service.impl;

import com.health.app.domain.Role;
import com.health.app.domain.UserAccount;
import com.health.app.dto.auth.LoginRequest;
import com.health.app.dto.auth.LoginResponse;
import com.health.app.dto.auth.RegisterRequest;
import com.health.app.exception.DuplicateEmailException;
import com.health.app.exception.EmailAlreadyRegisteredException;
import com.health.app.exception.PasswordMismatchException;
import com.health.app.repository.RoleRepository;
import com.health.app.repository.UserAccountRepository;
import com.health.app.service.AuthenticationService;
import com.health.app.service.EmailNormalizationService;
import com.health.app.service.PasswordService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserAccountRepository userAccountRepository;
    private final RoleRepository roleRepository;
    private final PasswordService passwordService;
    private final EmailNormalizationService emailNormalizationService;

    public AuthenticationServiceImpl(UserAccountRepository userAccountRepository,
                                     RoleRepository roleRepository, PasswordService passwordService,
                                     EmailNormalizationService emailNormalizationService) {
        this.userAccountRepository = userAccountRepository;
        this.roleRepository = roleRepository;
        this.passwordService = passwordService;
        this.emailNormalizationService = emailNormalizationService;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest){
        throw new UnsupportedOperationException("Login not implemented yet");
    }

    @Override
    @Transactional
    public void register(RegisterRequest registerRequest) {
        String email = registerRequest.email().trim();
        String normalizedEmail = emailNormalizationService.normalize(email);

        if (userAccountRepository.existsByNormalizedEmail(normalizedEmail)) {
            throw new DuplicateEmailException();
        }
        if(!registerRequest.password().equals(registerRequest.confirmPassword())){
            throw new PasswordMismatchException();
        }

        Role userRole = roleRepository.findById("USER").
                orElseThrow(() ->
                        new IllegalStateException("User role is not configured"));

        String passwordHash = passwordService.hash(registerRequest.password());

        UserAccount account = UserAccount.createPendingVerification(email,normalizedEmail,passwordHash,userRole);
        try {
            userAccountRepository.saveAndFlush(account);
        } catch(DataIntegrityViolationException exception) {
            if(userAccountRepository.existsByNormalizedEmail(normalizedEmail)){
                throw new EmailAlreadyRegisteredException();
            }
            throw exception;
        }
    }
}