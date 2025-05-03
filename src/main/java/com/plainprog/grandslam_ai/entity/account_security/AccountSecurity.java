package com.plainprog.grandslam_ai.entity.account_security;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "account_security")
public class AccountSecurity {
    @Id
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "hash_pass")
    private String hashPass;

    @Column(name = "verify_email_token")
    private String verifyEmailToken;

    @Column(name = "verify_email_token_created_at")
    private Instant verifyEmailTokenCreatedAt;

    public AccountSecurity() {
    }

    public AccountSecurity(Long accountId, String hashPass, String verifyEmailToken, Instant verifyEmailTokenCreatedAt) {
        this.accountId = accountId;
        this.hashPass = hashPass;
        this.verifyEmailToken = verifyEmailToken;
        this.verifyEmailTokenCreatedAt = verifyEmailTokenCreatedAt;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getHashPass() {
        return hashPass;
    }

    public void setHashPass(String hashPass) {
        this.hashPass = hashPass;
    }

    public String getVerifyEmailToken() {
        return verifyEmailToken;
    }

    public void setVerifyEmailToken(String verifyEmailToken) {
        this.verifyEmailToken = verifyEmailToken;
    }

    public Instant getVerifyEmailTokenCreatedAt() {
        return verifyEmailTokenCreatedAt;
    }

    public void setVerifyEmailTokenCreatedAt(Instant verifyEmailTokenCreatedAt) {
        this.verifyEmailTokenCreatedAt = verifyEmailTokenCreatedAt;
    }
}