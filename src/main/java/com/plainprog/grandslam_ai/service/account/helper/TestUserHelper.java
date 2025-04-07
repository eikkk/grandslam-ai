package com.plainprog.grandslam_ai.service.account.helper;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.account.AccountRepository;
import com.plainprog.grandslam_ai.entity.account_security.AccountSecurity;
import com.plainprog.grandslam_ai.entity.account_security.AccountSecurityRepository;
import com.plainprog.grandslam_ai.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TestUserHelper {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    AccountSecurityRepository accountSecurityRepository;

    @Value("${app.test.account.email}")
    private String testEmail;

    public Account ensureTestUserExists() {
        Account account = accountService.getAccountByEmail(testEmail);
        if (account == null) {
            account = accountService.createAccount(testEmail).getAccount();
        }
        return account;
    }

    public Account ensureTestUserExistAndNotVerified() {
        Account account = ensureTestUserExists();
        AccountSecurity security = accountSecurityRepository.findByAccountId(account.getId());
        if (security == null)
            throw new RuntimeException("Account security not found for account: " + account.getId());

        if (account.getEmailVerified()) {
            account.setEmailVerified(false);
            accountRepository.save(account);
        }
        if (security.getVerifyEmailToken() != null || security.getVerifyEmailTokenCreatedAt() != null) {
            security.setVerifyEmailToken(null);
            security.setVerifyEmailTokenCreatedAt(null);
            accountSecurityRepository.save(security);
        }
        return account;
    }

    public void clearTestUser() {
        Account account = accountService.getAccountByEmail(testEmail);
        if (account != null) {
            accountService.deleteAccount(account.getId());
        }
    }
}