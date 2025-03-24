package com.plainprog.grandslam_ai.entity.account_security;

import jakarta.persistence.*;

@Entity
@Table(name = "account_security")
public class AccountSecurity {
    @Id
    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "has_pass")
    private String hashPass;

    @Column(name = "pass_salt")
    private String passSalt;

    @Column(name = "verify_email_token")
    private Boolean verifyEmailToken;

    public AccountSecurity() {
    }

    public AccountSecurity(Integer accountId, String hashPass, String passSalt, Boolean verifyEmailToken) {
        this.accountId = accountId;
        this.hashPass = hashPass;
        this.passSalt = passSalt;
        this.verifyEmailToken = verifyEmailToken;
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

    public Boolean getVerifyEmailToken() {
        return verifyEmailToken;
    }

    public void setVerifyEmailToken(Boolean verifyEmailToken) {
        this.verifyEmailToken = verifyEmailToken;
    }
}