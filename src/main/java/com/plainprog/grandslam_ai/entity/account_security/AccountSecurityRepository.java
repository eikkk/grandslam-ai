package com.plainprog.grandslam_ai.entity.account_security;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountSecurityRepository extends JpaRepository<AccountSecurity, Long> {
    public AccountSecurity findByAccountId(Long accountId);
    public AccountSecurity findByVerifyEmailToken(String verifyEmailToken);
}