package com.plainprog.grandslam_ai.service.account;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.account.AccountRepository;
import com.plainprog.grandslam_ai.entity.account_security.AccountSecurity;
import com.plainprog.grandslam_ai.entity.account_security.AccountSecurityRepository;
import com.plainprog.grandslam_ai.object.dto.auth.AccountCreationDTO;
import com.plainprog.grandslam_ai.object.dto.util.SimpleOperationResultDTO;
import com.plainprog.grandslam_ai.service.account.helper.PassGenHelp;
import com.plainprog.grandslam_ai.service.email.EmailServiceJavaMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountSecurityRepository accountSecurityRepository;
    @Autowired
    private EmailServiceJavaMail emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.url.base}")
    private String baseUrl;
    @Value("${app.brand.name}")
    private String appName;

    @Value("${app.test.account.email}")
    private String testEmail;
    @Value("${app.test.account.password}")
    private String testAccPass;

    /**
     * Given the email creates an account and account_security. Generates random password
     * To be used for the very first account creation.
     * @param email email of the account
     * @return account creation dto with account, account security, password and error message
     */
    public AccountCreationDTO createAccount(String email) {
        Account existingAccount = accountRepository.findByEmail(email);
        if (existingAccount != null ) {
            return new AccountCreationDTO(null, null, null, "Account with this email already exists");
        }

        String pass = testEmail.equals(email) ? testAccPass : PassGenHelp.randomPassword();
        String hashPass = passwordEncoder.encode(pass);

        Account acc = new Account(email, false, Instant.now());
        Account account = accountRepository.save(acc);

        AccountSecurity accSec = new AccountSecurity(account.getId(), hashPass, null, null);
        accountSecurityRepository.save(accSec);

        return new AccountCreationDTO(account, accSec, pass, null);
    }
    /**
     * To be used from the verification web page.
     * Verifies the token that has been passed in the link.
     * Sets emailVerified to true if successful + sets the email verification token to null.
     * @param token token to verify email and check for expiration
     * @return operation result dto with success status and message
     */
    public SimpleOperationResultDTO verifyEmailToken(String token) {
        AccountSecurity accountSecurity = accountSecurityRepository.findByVerifyEmailToken(token);
        if (accountSecurity == null) {
            return new SimpleOperationResultDTO(false, "Invalid link");
        }
        //handle expiration time
        Instant tokenCreatedAt = accountSecurity.getVerifyEmailTokenCreatedAt();
        int expirationTime = 60 * 60 * 36;//36 hours
        if (tokenCreatedAt.plusSeconds(expirationTime).isBefore(Instant.now())) {
            return new SimpleOperationResultDTO(false, "Link expired. Please request new verification email inside app");
        }

        Account account = accountRepository.findById(accountSecurity.getAccountId()).orElse(null);
        if (account == null) {
            return new SimpleOperationResultDTO(false, "Account does not exist");
        }
        if (account.getEmailVerified()) {
            return new SimpleOperationResultDTO(false, "Email already verified");
        }
        account.setEmailVerified(true);
        accountSecurity.setVerifyEmailToken(null);
        accountSecurity.setVerifyEmailTokenCreatedAt(null);
        accountRepository.save(account);
        accountSecurityRepository.save(accountSecurity);
        return new SimpleOperationResultDTO(true, "Email verified successfully");
    }
    /**
     * Sends a verification email to the account with the given email.
     * @param email email of the account
     * @return operation result dto with success status and message
     */
    public SimpleOperationResultDTO sendVerificationEmail(String email) {
        Account account = accountRepository.findByEmail(email);
        if (account == null ) {
            return new SimpleOperationResultDTO(false, "Account with this email does not exist");
        }
        if (account.getEmailVerified()){
            return new SimpleOperationResultDTO(false, "Email already verified");
        }
        AccountSecurity accountSecurity = accountSecurityRepository.findByAccountId(account.getId());
        if (accountSecurity == null) {
            return new SimpleOperationResultDTO(false, "Account security error");
        }

        String token = PassGenHelp.randomEmailVerificationToken();
        String uriEncodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
        String link = baseUrl + "/verification?token=" + uriEncodedToken;
        Map<String,Object> model = Map.of("link", link);
        try {
            emailService.sendTemplateEmail(email, "Verification", model, "VerificationEmail.ftl");
        } catch (Exception e) {
            return new SimpleOperationResultDTO(false, "Email sending failed", e.getMessage());
        }
        accountSecurity.setVerifyEmailToken(token);
        accountSecurity.setVerifyEmailTokenCreatedAt(Instant.now());
        accountSecurityRepository.save(accountSecurity);
        return new SimpleOperationResultDTO(true, "Verification email sent successfully", token);
    }
    /**
     * Sends a registration email to the account with the given email.
     * Registration email has verification button with verification link. Also contains the password.
     * @param account account to send email to
     * @param accountSecurity account security to update the verify email token
     * @param pass password to send in the email
     */
    public SimpleOperationResultDTO sendRegistrationEmail(Account account, AccountSecurity accountSecurity, String pass) {
        String token = PassGenHelp.randomEmailVerificationToken();
        String uriEncodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
        String link = "http://localhost:8080/verification?token=" + uriEncodedToken;
        Map<String,Object> model = Map.of("password", pass, "link", link);
        boolean sent = emailService.sendTemplateEmail(account.getEmail(), "Verification for " + appName, model, "VerificationAndPasswordEmail.ftl");
        if (!sent) {
            return new SimpleOperationResultDTO(false, "Email sending failed");
        }
        accountSecurity.setVerifyEmailToken(token);
        accountSecurity.setVerifyEmailTokenCreatedAt(Instant.now());
        accountSecurityRepository.save(accountSecurity);
        return new SimpleOperationResultDTO(true, "Registration email sent successfully");
    }
    /**
     * Finds an account by email.
     * @param email email of the account
     * @return account with the given email
     */
    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }
    /**
     * Deletes account by id
     * @param id id of the account
     */
    public void deleteAccount(Integer id) {
        accountRepository.deleteById(id);
    }
}
