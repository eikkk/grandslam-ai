package com.plainprog.grandslam_ai.image_management.incubator;


import com.plainprog.grandslam_ai.config.TestConfig;
import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.account.AccountRepository;
import com.plainprog.grandslam_ai.entity.account_security.AccountSecurity;
import com.plainprog.grandslam_ai.entity.account_security.AccountSecurityRepository;
import com.plainprog.grandslam_ai.entity.img_gen.Image;
import com.plainprog.grandslam_ai.entity.img_management.IncubatorEntry;
import com.plainprog.grandslam_ai.entity.img_management.IncubatorEntryRepository;
import com.plainprog.grandslam_ai.object.dto.util.OperationOutcome;
import com.plainprog.grandslam_ai.object.dto.util.OperationResultDTO;
import com.plainprog.grandslam_ai.object.request_models.auth.CreateAccountRequest;
import com.plainprog.grandslam_ai.object.request_models.auth.LoginRequest;
import com.plainprog.grandslam_ai.object.request_models.generation.SeedRegenRequest;
import com.plainprog.grandslam_ai.object.request_models.other.BatchOperationOnLongIds;
import com.plainprog.grandslam_ai.object.response_models.generation.ImgGenResponse;
import com.plainprog.grandslam_ai.service.account.AccountService;
import com.plainprog.grandslam_ai.service.account.helper.TestUserHelper;
import com.plainprog.grandslam_ai.service.gcp.GCPStorageService;
import com.plainprog.grandslam_ai.service.generation.TestGenerationHelper;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {TestConfig.class})
public class IncubatorTests {

    @Value("${app.url.base}")
    private String baseUrl;
    @Value("${app.test.account.email}")
    private String testEmail;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private IncubatorEntryRepository incubatorEntryRepository;
    @Autowired
    private TestGenerationHelper testGenerationHelper;
    @Autowired
    private GCPStorageService gcpStorageService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountSecurityRepository accountSecurityRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TestUserHelper testUserHelper;

    private List<IncubatorEntry> testEntries;

    @BeforeEach
    public void ensureEntriesExist() throws Exception {
        //Ensures there are at least two incubator images of test user
        Account testAcc = testUserHelper.ensureTestUserExists();
        List<IncubatorEntry> entries = incubatorEntryRepository.findByImageOwnerAccountId(testAcc.getId());
        if (entries.size() < 2) {
            // Create two test entries
            List<Long> imageIds = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                ImgGenResponse response = testGenerationHelper.produceTestUserImage();
                imageIds.add(response.getImageId());
            }
            testEntries = incubatorEntryRepository.findAllByImageIdIn(imageIds);
        } else {
            //take two entries
            testEntries = entries.subList(0, 2);
        }
    }
    @Test
    public void testIncubatorDeletion() throws Exception {
        assertTrue(testEntries.size() > 1, "Not enough entries to test deletion");
        List<Long> incubatorEntryIds = testEntries.stream()
                .map(entry -> entry.getImage().getId())
                .toList();
        List<Image> images = testEntries.stream()
                .map(IncubatorEntry::getImage)
                .toList();
        List<Long> imageIds = images.stream()
                .map(Image::getId)
                .toList();
        List<String> allUrls = images.stream()
                .flatMap(image -> Stream.of(image.getThumbnail(), image.getFullsize(), image.getCompressed()))
                .toList();
        //verify that all urls exist
        for (String url : allUrls) {
            assertTrue(gcpStorageService.exists(url), "Image not found in GCP: " + url);
        }
        //make unauthenticated delete request
        BatchOperationOnLongIds request = new BatchOperationOnLongIds(imageIds);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<BatchOperationOnLongIds> entity = new HttpEntity<>(request, headers);


        //Test that endpoint is protected by authentication
        ResponseEntity<OperationResultDTO> responseEntity =
                restTemplate.postForEntity(baseUrl + "/api/incubator/batch/delete", entity, OperationResultDTO.class);

        //Request not allowed
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());

        // Now let's log in the user
        HttpHeaders headersWithAuth = testUserHelper.initiateSession();
        headersWithAuth.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        assertTrue(headersWithAuth.containsKey("Cookie"));

        // Now we can access the endpoint with our headers
        HttpEntity<BatchOperationOnLongIds> entityWithAuth = new HttpEntity<>(request, headersWithAuth);
        responseEntity =
                restTemplate.postForEntity(baseUrl + "/api/incubator/batch/delete", entityWithAuth, OperationResultDTO.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        //verify that incubator entries are deleted
        for (Long id : incubatorEntryIds) {
            assertFalse(incubatorEntryRepository.existsById(id), "Incubator entry not deleted: " + id);
        }
        //verify that images are deleted
        for (Long id : imageIds) {
            assertFalse(incubatorEntryRepository.existsById(id), "Image not deleted: " + id);
        }
        //verify that images urls are deleted from gcp
        for (String url : allUrls) {
            assertFalse(gcpStorageService.exists(url), "Image not deleted from GCP: " + url);
        }

    }
}
