package com.plainprog.grandslam_ai.generation;

import com.plainprog.grandslam_ai.config.TestConfig;
import com.plainprog.grandslam_ai.object.request_models.generation.ImgGenRequest;
import com.plainprog.grandslam_ai.object.response_models.generation.ImgGenModulesResponse;
import com.plainprog.grandslam_ai.object.response_models.generation.ModulesHealthCheckResponse;
import com.plainprog.grandslam_ai.service.account.helper.TestUserHelper;
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
public class ModulesTest {
    @Value("${app.url.base}")
    private String baseUrl;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TestUserHelper testUserHelper;

    @Test
    public void modulesTest() throws Exception {
        // When
        ResponseEntity<ModulesHealthCheckResponse> responseEntity =
                restTemplate.postForEntity(baseUrl + "/api/gen/modules/health_check", null, ModulesHealthCheckResponse.class);

        // Request succeeded
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Make request to modules endpoint
        HttpHeaders headersWithAuth = testUserHelper.initiateSession();
        assertTrue(headersWithAuth.containsKey("Cookie"));

        // Now we can access the endpoint with our headers
        HttpEntity<ImgGenRequest> entityWithAuth = new HttpEntity<>(headersWithAuth);
        ResponseEntity<ImgGenModulesResponse> responseEntityModules =
                restTemplate.exchange(baseUrl + "/api/gen/modules", HttpMethod.GET, entityWithAuth, ImgGenModulesResponse.class);

        // Request succeeded
        assertEquals(HttpStatus.OK, responseEntityModules.getStatusCode());
        ImgGenModulesResponse response = responseEntityModules.getBody();
        assertNotNull(response);
        assertNotNull(response.getGroups());
        assertFalse(response.getGroups().isEmpty());
    }

}
