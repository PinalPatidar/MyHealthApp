package com.health.app.repository;

import com.health.app.domain.Role;
import com.health.app.domain.UserAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=validate"
})
public class UserAccountRepositoryTest {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void shouldFindUserByNormalizedEmail() {

        Role userRole = roleRepository.findById("USER")
                .orElseThrow();

        UserAccount account = UserAccount.createPendingVerification("Pinal@Example.COM",
                "pinal@example.com",
                "argon2-hashed-password",
                userRole);

        userAccountRepository.save(account);

        var result = userAccountRepository.findByNormalizedEmail("pinal@example.com");

        assertTrue(result.isPresent());
        assertEquals("Pinal@Example.COM", result.get().getEmail());
        assertEquals("pinal@example.com", result.get().getNormalizedEmail());
        assertEquals("USER", result.get().getRole().getCode());
    }

    @Test
    void shouldReturnEmptyWhenNormalizedEmailDoesNotExist(){
        var result = userAccountRepository
                .findByNormalizedEmail("doesnotexist@example.com");
        assertTrue(result.isEmpty());
    }
}