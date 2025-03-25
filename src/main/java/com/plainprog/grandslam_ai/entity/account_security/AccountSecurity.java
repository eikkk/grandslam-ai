package com.plainprog.grandslam_ai.entity.account_security;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "account_security")
public class AccountSecurity {
    @Id
    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "hash_pass")
    private String hashPass;

    @Column(name = "pass_salt")
    private String passSalt;

    @Column(name = "verify_email_token")
    private String verifyEmailToken;

    @Column(name = "verify_email_token_created_at")
    private Instant verifyEmailTokenCreatedAt;

    public AccountSecurity() {
    }

    public AccountSecurity(Integer accountId, String hashPass, String passSalt, String verifyEmailToken, Instant verifyEmailTokenCreatedAt) {
        this.accountId = accountId;
        this.hashPass = hashPass;
        this.passSalt = passSalt;
        this.verifyEmailToken = verifyEmailToken;
        this.verifyEmailTokenCreatedAt = verifyEmailTokenCreatedAt;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getHashPass() {
        return hashPass;
    }

    public void setHashPass(String hashPass) {
        this.hashPass = hashPass;
    }

    public String getPassSalt() {
        return passSalt;
    }

    public void setPassSalt(String passSalt) {
        this.passSalt = passSalt;
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