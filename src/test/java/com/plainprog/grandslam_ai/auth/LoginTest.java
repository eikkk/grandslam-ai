package com.plainprog.grandslam_ai.auth;

import com.plainprog.grandslam_ai.config.TestConfig;
import com.plainprog.grandslam_ai.object.dto.auth.AccountWithPasswordDTO;
import com.plainprog.grandslam_ai.object.request_models.auth.LoginRequest;
import com.plainprog.grandslam_ai.service.account.helper.TestHelper;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {TestConfig.class})
public class LoginTest {

    @Value("${app.url.base}")
    private String baseUrl;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TestHelper testHelper;

    private AccountWithPasswordDTO testAcc;


    @BeforeEach
    public void setUpTestUser() {
        //Ensure test acc exist is not verified and the email token does not exist
        testAcc = testHelper.ensureTestUserExists();
        assertNotNull(testAcc);
    }
    @Test
    public void testLogin() throws Exception {
        try{
            // Verify that wrong password fails
            LoginRequest loginRequest = new LoginRequest(testAcc.getAccount().getEmail(), "wrongpassword");
            ResponseEntity<String> responseEntity =
                    restTemplate.postForEntity(baseUrl + "/api/auth/login", loginRequest, String.class);

            // Request fails
            assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

            // Verify that correct password succeeds
            loginRequest = new LoginRequest(testAcc.getAccount().getEmail(), testAcc.getPassword());
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
