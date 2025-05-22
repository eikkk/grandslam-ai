package com.plainprog.grandslam_ai.generation;

import com.plainprog.grandslam_ai.BaseEndpointTest;
import com.plainprog.grandslam_ai.object.response_models.generation.ImgGenModulesResponse;
import com.plainprog.grandslam_ai.object.response_models.generation.ModulesHealthCheckResponse;
import com.plainprog.grandslam_ai.service.account.helper.TestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

public class ModulesTest extends BaseEndpointTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TestHelper testHelper;

    @Test
    public void healthCheckTest() throws Exception {
        // When
        ResponseEntity<ModulesHealthCheckResponse> responseEntity =
                restTemplate.postForEntity(baseUrl + "/api/gen/modules/health_check", null, ModulesHealthCheckResponse.class);
        // Request succeeded
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    @Test
    public void modulesEndpointTest() throws Exception {
        String url = baseUrl + "/api/gen/modules";
        HttpMethod method = HttpMethod.GET;
        Class<?> responseType = ImgGenModulesResponse.class;
        // Test if the endpoint is protected by authentication
        testEndpointProtection(url, method, null, responseType);
        // Test if the endpoint returns a 200 OK status
        ResponseEntity<?> response = testAuthenticatedRequest(url, method, null, responseType);

        ImgGenModulesResponse body = (ImgGenModulesResponse)response.getBody();
        assertNotNull(body);
        assertNotNull(body.getGroups());
        assertFalse(body.getGroups().isEmpty());
    }

}
