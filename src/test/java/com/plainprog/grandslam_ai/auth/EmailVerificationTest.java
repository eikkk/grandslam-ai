package com.plainprog.grandslam_ai.auth;

import com.plainprog.grandslam_ai.config.TestConfig;
import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.account.AccountRepository;
import com.plainprog.grandslam_ai.entity.account_security.AccountSecurity;
import com.plainprog.grandslam_ai.entity.account_security.AccountSecurityRepository;
import com.plainprog.grandslam_ai.object.dto.util.OperationOutcome;
import com.plainprog.grandslam_ai.object.dto.util.OperationResultDTO;
import com.plainprog.grandslam_ai.object.request_models.auth.CreateAccountRequest;
import com.plainprog.grandslam_ai.object.request_models.auth.LoginRequest;
import com.plainprog.grandslam_ai.service.account.AccountService;
import com.plainprog.grandslam_ai.service.account.helper.TestUserHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {TestConfig.class})
public class EmailVerificationTest {

    @Value("${app.url.base}")
    private String baseUrl;
    @Value("${app.test.account.email}")
    private String testEmail;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountSecurityRepository accountSecurityRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TestUserHelper testUserHelper;

    @BeforeEach
    public void setUpTestUser() {
        //Ensure test acc exist is not verified and the email token does not exist
        Account testAcc = testUserHelper.ensureTestUserExistAndNotVerified();
        assertNotNull(testAcc);
        assertFalse(testAcc.getEmailVerified());
    }
    @Test
    public void testEmailVerification() throws Exception {
        Account account = null;
        try{
            // First we have to verify that not logged-in user can't access the endpoint
            // When
            ResponseEntity<OperationResultDTO> responseEntity =
                    restTemplate.postForEntity(baseUrl + "/api/auth/account/email-verification", null, OperationResultDTO.class);

            // Request fails
            assertNotEquals(HttpStatus.OK, responseEntity.getStatusCode());

            // Now let's log in the user
            HttpHeaders headers = testUserHelper.initiateSession();
            assertTrue(headers.containsKey("Cookie"));

            // Now we can access the endpoint with our headers
            HttpEntity<OperationResultDTO> entity = new HttpEntity<>(headers);
            responseEntity =
                    restTemplate.postForEntity(baseUrl + "/api/auth/account/email-verification", entity, OperationResultDTO.class);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());


            account = accountService.getAccountByEmail(testEmail);
            // Email verification token gets generated
            AccountSecurity accountSecurity = accountSecurityRepository.findByAccountId(account.getId());
            assertNotNull(accountSecurity);
            assertNotNull(accountSecurity.getVerifyEmailToken());
            assertNotNull(accountSecurity.getVerifyEmailTokenCreatedAt());
            assertFalse(account.getEmailVerified());
            assertTrue(accountSecurity.getVerifyEmailTokenCreatedAt().isAfter(Instant.now().minusSeconds(60)));
            assertTrue(accountSecurity.getVerifyEmailToken().length() > 5);

            // Imitate email verification link opening
            String token =  URLEncoder.encode(accountSecurity.getVerifyEmailToken(), StandardCharsets.UTF_8);
            String verifyUrl = baseUrl + "/verification?token=" + token;
            ResponseEntity<String> verifyResponseEntity =
                    restTemplate.getForEntity(verifyUrl, String.class);

            // Verify HTTP status
            assertEquals(200, verifyResponseEntity.getStatusCode().value());

            // Check the page content
            String responseBody = verifyResponseEntity.getBody();
            assertNotNull(responseBody);
            assertTrue(responseBody.contains("Your email is confirmed"));
        } finally {
            //Put clean up here if needed
        }


    }
}
