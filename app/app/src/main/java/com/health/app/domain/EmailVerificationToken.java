package com.health.app.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "email_verification_tokens")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "token_hash" , nullable = false , unique = true , length=64)
    private String tokenHash;

    @Column(name = "expires_at" , nullable = false)
    private Instant expiresAt;

    @Column(name = "used_at")
    private Instant usedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional=false)
    @JoinColumn(name = "user_account_id" , nullable = false)
    private UserAccount userAccount;

    public static EmailVerificationToken create(
            String tokenHash,
            Instant expiresAt,
            UserAccount userAccount){
        EmailVerificationToken token = new EmailVerificationToken();
        token.tokenHash = tokenHash;
        token.expiresAt = expiresAt;
        token.userAccount = userAccount;
        return token;
    }
}
