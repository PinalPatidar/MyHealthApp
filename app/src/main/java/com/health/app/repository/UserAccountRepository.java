package com.health.app.repository;

import com.health.app.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {

    Optional<UserAccount> findByEmail(String email);
    Optional<UserAccount> findByNormalizedEmail(String normalizedEmail);
    boolean existsByEmail(String email);
    boolean existsByNormalizedEmail(String normalizedEmail);

}
