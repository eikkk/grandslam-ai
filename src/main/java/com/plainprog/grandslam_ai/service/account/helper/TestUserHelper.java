package com.plainprog.grandslam_ai.service.account.helper;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.account.AccountRepository;
import com.plainprog.grandslam_ai.entity.account_security.AccountSecurity;
import com.plainprog.grandslam_ai.entity.account_security.AccountSecurityRepository;
import com.plainprog.grandslam_ai.object.request_models.auth.LoginRequest;
import com.plainprog.grandslam_ai.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TestUserHelper {
    @Value("${app.url.base}")
    private String baseUrl;
    @Value("${app.test.account.email}")
    private String testEmail;
    @Value("${app.test.account.password}")
    private String testAccPassword;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    AccountSecurityRepository accountSecurityRepository;
    @Autowired
    private RestTemplate restTemplate;


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

    public HttpHeaders initiateSession() {
        Account account = ensureTestUserExists();
        if (account == null) {
            throw new RuntimeException("Test user not found");
        }

        ResponseEntity<String> loginResponseEntity =
                restTemplate.postForEntity(baseUrl + "/api/auth/login", new LoginRequest(testEmail, testAccPassword), String.class);

        String sessionCookie = loginResponseEntity.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        if (sessionCookie == null) {
            throw new RuntimeException("Failed to initiate session");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Cookie", sessionCookie);
        return headers;
    }
}