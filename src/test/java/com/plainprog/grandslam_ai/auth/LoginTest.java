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
public class LoginTest {

    @Value("${app.url.base}")
    private String baseUrl;
    @Value("${app.test.account.email}")
    private String testEmail;
    @Value("${app.test.account.password}")
    private String testPassword;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TestUserHelper testUserHelper;

    @BeforeEach
    public void setUpTestUser() {
        //Ensure test acc exist is not verified and the email token does not exist
        Account account = testUserHelper.ensureTestUserExists();
        assertNotNull(account);
    }
    @Test
    public void testLogin() throws Exception {
        try{
            // Verify that wrong password fails
            LoginRequest loginRequest = new LoginRequest(testEmail, "wrongpassword");
            ResponseEntity<String> responseEntity =
                    restTemplate.postForEntity(baseUrl + "/api/auth/login", loginRequest, String.class);

            // Request fails
            assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

            // Verify that correct password succeeds
            loginRequest = new LoginRequest(testEmail, testPassword);
            responseEntity =
                    restTemplate.postForEntity(baseUrl + "/api/auth/login", loginRequest, String.class);

            // Request succeeds
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            // Verify that the response contains a session cookie
            assertTrue(responseEntity.getHeaders().containsKey("set-cookie"));
        } finally {
            //Put clean up here if needed
        }


    }
}
