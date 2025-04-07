package com.plainprog.grandslam_ai.auth;

import com.plainprog.grandslam_ai.config.TestConfig;
import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.account.AccountRepository;
import com.plainprog.grandslam_ai.entity.account_security.AccountSecurityRepository;
import com.plainprog.grandslam_ai.object.dto.util.OperationOutcome;
import com.plainprog.grandslam_ai.object.dto.util.OperationResultDTO;
import com.plainprog.grandslam_ai.object.request_models.auth.CreateAccountRequest;
import com.plainprog.grandslam_ai.service.account.AccountService;
import com.plainprog.grandslam_ai.service.account.helper.TestUserHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {TestConfig.class})
public class AccountCreationTest {

    @Value("${app.url.base}")
    private String baseUrl;
    @Value("${app.test.account.email}")
    private String testEmail;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountSecurityRepository accountSecurityRepository;
    @Autowired
    private TestUserHelper testUserHelper;


    @BeforeEach
    public void clearTestUser() {
        testUserHelper.clearTestUser();
    }


    //TODO: ENVIRONMENT SETUP FOR TESTS TO NOT RUN ON PROD
    @Test
    public void testCreateAccountEndpoint() throws Exception {
        Account account = null;
        try{
            // Given
            CreateAccountRequest request = new CreateAccountRequest(testEmail);

            // When
            ResponseEntity<OperationResultDTO> responseEntity =
                    restTemplate.postForEntity(baseUrl + "/api/auth/account", request, OperationResultDTO.class);

            // Request succeeded
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

            //Verify that email has been sent
            OperationResultDTO result = responseEntity.getBody();
            assertNotNull(result);
            assertEquals(OperationOutcome.SUCCESS, result.getOperationOutcome());

            // Find by email in DB
            account = accountRepository.findByEmail(request.getEmail());
            assertNotNull(account);
            assertEquals(testEmail, account.getEmail());


            // Verify account_security entry is created along with password
            var accountSecurity = accountSecurityRepository.findByAccountId(account.getId());
            assertNotNull(accountSecurity);
            assertNotNull(accountSecurity.getHashPass());
            assertNotNull(accountSecurity.getVerifyEmailToken());

            // Verify time of email token creation is less than a minute ago
            Instant tokenCreationTime = accountSecurity.getVerifyEmailTokenCreatedAt();
            Instant currentTime = Instant.now();
            assertNotNull(tokenCreationTime);
            assertTrue(tokenCreationTime.plusSeconds(60).isAfter(currentTime));
        } finally {
            // Clean up
            // Remove the test account
            Account acc = accountRepository.findByEmail(testEmail);
            accountRepository.delete(acc);
            // Verify account does not exist in DB
            Account accountRemoved = accountRepository.findByEmail(testEmail);
            assertNull(accountRemoved);
        }


    }
}