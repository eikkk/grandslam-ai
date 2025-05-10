package com.plainprog.grandslam_ai;


import com.plainprog.grandslam_ai.config.TestConfig;
import com.plainprog.grandslam_ai.service.account.helper.TestUserHelper;
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
public abstract class BaseEndpointTest {
    @Autowired
    private TestUserHelper testUserHelper;
    @Value("${app.url.base}")
    protected String baseUrl;

    @Autowired
    protected TestRestTemplate restTemplate;

    /**
     * Tests if an endpoint is protected by authentication.
     *
     * @param url The endpoint URL to test.
     * @param method The HTTP method to use (e.g., GET, POST).
     * @param requestBody The request body to send (can be null for GET).
     */
    protected void testEndpointProtection(String url, HttpMethod method, Object requestBody, Class<?> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<?> response = restTemplate.exchange(url, method, entity, responseType);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Endpoint is not protected by authentication");
    }

    /**
     * Tests if an endpoint returns a 200 OK status.
     *
     * @param url The endpoint URL to test.
     * @param method The HTTP method to use (e.g., GET, POST).
     * @param requestBody The request body to send (can be null for GET).
     */
    protected ResponseEntity<?> testAuthenticatedRequest(String url, HttpMethod method, Object requestBody, Class<?> responseType) {
        HttpHeaders headersWithAuth = testUserHelper.initiateSession();
        headersWithAuth.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Object> entity = new HttpEntity<>(requestBody, headersWithAuth);

        ResponseEntity<?> response = restTemplate.exchange(url, method, entity, responseType);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Endpoint did not return 200 OK");
        return response;
    }
    /**
     * Tests to verify that endpoint returns a 500
     *
     * @param url The endpoint URL to test.
     * @param method The HTTP method to use (e.g., GET, POST).
     * @param requestBody The request body to send (can be null for GET).
     * @param responseType The expected response type.
     */
    protected void testForPositiveExpectedError(String url, HttpMethod method, Object requestBody, Class<?> responseType) {
        HttpHeaders headersWithAuth = testUserHelper.initiateSession();
        headersWithAuth.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Object> entity = new HttpEntity<>(requestBody, headersWithAuth);

        ResponseEntity<?> response = restTemplate.exchange(url, method, entity, responseType);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(), "Endpoint did not return 500");
    }
}