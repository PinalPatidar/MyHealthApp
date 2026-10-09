package com.health.app.service;

import com.health.app.domain.Role;
import com.health.app.domain.UserAccount;
import com.health.app.dto.auth.RegisterRequest;
import com.health.app.exception.DuplicateEmailException;
import com.health.app.exception.PasswordMismatchException;
import com.health.app.repository.RoleRepository;
import com.health.app.repository.UserAccountRepository;
import com.health.app.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordService passwordService;

    @Mock
    private EmailNormalizationService emailNormalizationService;

    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp(){
        authenticationService = new AuthenticationServiceImpl(userAccountRepository,
                roleRepository, passwordService,
                emailNormalizationService);
    }

    @Test
    void shouldRegisterNewUser(){
        RegisterRequest request = new RegisterRequest("  Pinal@Example.COM  ",
                "StrongPassword@123",
                "StrongPassword@123");

        Role userRole = new Role();

        when(emailNormalizationService.normalize("Pinal@Example.COM"))
                .thenReturn("pinal@example.com");

        when(userAccountRepository.existsByNormalizedEmail("pinal@example.com"))
                .thenReturn(false);

        when(roleRepository.findById("USER"))
                .thenReturn(Optional.of(userRole));

        when(passwordService.hash("StrongPassword@123"))
                .thenReturn("argon2-hashed-password");

        authenticationService.register(request);

        ArgumentCaptor<UserAccount> accountCeptor = ArgumentCaptor.forClass(UserAccount.class);

        verify(userAccountRepository).saveAndFlush(accountCeptor.capture());

        UserAccount savedAccount = accountCeptor.getValue();

        assertEquals("Pinal@Example.COM", savedAccount.getEmail());
        assertEquals("pinal@example.com", savedAccount.getNormalizedEmail());
        assertEquals("argon2-hashed-password", savedAccount.getPasswordHash());
        assertEquals(userRole, savedAccount.getRole());
        assertEquals(com.health.app.domain.AccountStatus.PENDING_VERIFICATION,
                savedAccount.getStatus()
        );
        assertFalse(savedAccount.isEmailVerified());
        assertEquals(0,savedAccount.getFailedLoginAttempts());
   }

    @Test
    void shouldRejectDuplicateEmail(){
        RegisterRequest request = new RegisterRequest("Pinal@Example.COM",
                "StrongPassword@123",
                "StrongPassword@123");

        when(emailNormalizationService.normalize("Pinal@Example.COM"))
                .thenReturn("pinal@example.com");

        when(userAccountRepository.existsByNormalizedEmail("pinal@example.com"))
                .thenReturn(true);

        assertThrows(
                DuplicateEmailException.class,
                ()->authenticationService.register(request)
        );
        verify(userAccountRepository, never()).save(any());
        verify(passwordService, never()).hash(any());
    }

    @Test
    void shouldRejectPasswordMismatch(){
        RegisterRequest request = new RegisterRequest(
                "pinal@example.com",
                "StrongPassword@123",
                "DifferentPassword@123"
        );

        when(emailNormalizationService.normalize("pinal@example.com"))
                .thenReturn("pinal@example.com");

        when(userAccountRepository.existsByNormalizedEmail("pinal@example.com"))
                .thenReturn(false);

        assertThrows(PasswordMismatchException.class,
                ()->authenticationService.register(request)
        );

        verify(userAccountRepository,never()).save(any());
        verify(passwordService,never()).hash(any());
        verify(roleRepository, never()).findById(any());

    }
}
