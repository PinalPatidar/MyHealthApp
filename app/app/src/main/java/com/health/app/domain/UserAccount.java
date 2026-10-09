package com.health.app.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Entity
@Table(name="user_accounts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length=320)
    private String email;

    @Column(name="normalized_email", nullable = false, unique = true, length=320)
    private String normalizedEmail;

    @Column(name="password_hash", length=255)
    private String passwordHash;

    @ManyToOne(fetch=FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_code", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private AccountStatus status=AccountStatus.INVITED;

    @Column(name="email_verified", nullable = false)
    private boolean emailVerified;

    @Column(name="failed_login_attempts", nullable = false)
    private int failedLoginAttempts;

    public static UserAccount createPendingVerification(String email, String normalizedEmail, String passwordHash, Role role){
        UserAccount account = new UserAccount();

        account.email = email;
        account.normalizedEmail = normalizedEmail;
        account.passwordHash = passwordHash;
        account.role = role;
        account.status = AccountStatus.PENDING_VERIFICATION;
        account.emailVerified = false;
        account.failedLoginAttempts = 0;

        return account;
    }

}
