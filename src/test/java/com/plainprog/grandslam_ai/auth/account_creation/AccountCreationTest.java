package com.plainprog.grandslam_ai.auth.account_creation;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.account.AccountRepository;
import com.plainprog.grandslam_ai.entity.account_security.AccountSecurityRepository;
import com.plainprog.grandslam_ai.object.dto.util.OperationOutcome;
import com.plainprog.grandslam_ai.object.dto.util.OperationResultDTO;
import com.plainprog.grandslam_ai.object.request_models.auth.CreateAccountRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountCreationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountSecurityRepository accountSecurityRepository;

    //TODO: ENVIRONMENT SETUP FOR TESTS TO NOT RUN ON PROD
    @Test
    public void testCreateAccountEndpoint() throws Exception {
        String email = "test_test_test@example.com";
        Account account = null;
        try{
            // Given
            CreateAccountRequest request = new CreateAccountRequest(email);

            // When
            //FIXME: URL
            ResponseEntity<OperationResultDTO> responseEntity =
                    restTemplate.postForEntity("http://localhost:8080/api/auth/account", request, OperationResultDTO.class);

            // Request succeeded
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

            //Verify that email has been sent
            OperationResultDTO result = responseEntity.getBody();
            assertNotNull(result);
            assertEquals(OperationOutcome.SUCCESS, result.getOperationOutcome());

            // Find by email in DB
            account = accountRepository.findByEmail(request.getEmail());
            assertNotNull(account);
            assertEquals(email, account.getEmail());


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
            Account acc = accountRepository.findByEmail(email);
            accountRepository.delete(acc);
            // Verify account does not exist in DB
            Account accountRemoved = accountRepository.findByEmail(email);
            assertNull(accountRemoved);
        }


    }
}