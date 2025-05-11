package com.plainprog.grandslam_ai.image_management.gallery;

import com.plainprog.grandslam_ai.BaseEndpointTest;
import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.img_management.GalleryGroup;
import com.plainprog.grandslam_ai.entity.img_management.GalleryGroupRepository;
import com.plainprog.grandslam_ai.object.dto.util.OperationOutcome;
import com.plainprog.grandslam_ai.object.dto.util.OperationResultDTO;
import com.plainprog.grandslam_ai.object.request_models.gallery.CreateGalleryGroupRequest;
import com.plainprog.grandslam_ai.object.request_models.gallery.UpdateGalleryRequest;
import com.plainprog.grandslam_ai.service.account.helper.TestUserHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;
public class GalleryTests extends BaseEndpointTest {

    @Autowired
    private GalleryGroupRepository galleryGroupRepository;
    @Autowired
    private TestUserHelper testUserHelper;

    private Account testAccount;

    @BeforeEach
    public void setUp() throws Exception {
        // Ensure test user exists
        testAccount = testUserHelper.ensureTestUserExists();
    }

    @Test
    public void testCreateGalleryGroup() {
        String url = baseUrl + "/gallery/groups/new";
        HttpMethod method = HttpMethod.POST;
        Class<?> responseType = OperationResultDTO.class;
        //null names are also allowed, but not covered by this test
        String groupName = "Test Gallery Group " + System.currentTimeMillis();
        // Create request body
        UpdateGalleryRequest request = new UpdateGalleryRequest(groupName);

        // Test unauthenticated access
        testEndpointProtection(url, method, request, responseType);

        // Test authenticated access
        ResponseEntity<?> response = testAuthenticatedRequest(url + "?name=" + groupName, method, request, responseType);

        // Verify response
        assertNotNull(response.getBody(), "Response body is null");
        OperationResultDTO responseBody = (OperationResultDTO) response.getBody();
        assertEquals(com.plainprog.grandslam_ai.object.dto.util.OperationOutcome.SUCCESS, responseBody.getOperationOutcome(), "Operation outcome is not success");

        // Get from DB most recently created group with given account_id
        var groups = galleryGroupRepository.findAllByAccountId(testAccount.getId());
        GalleryGroup createdGroup = groups.stream()
                .filter(group -> group.getName().equals(groupName))
                .findFirst()
                .orElse(null);
        assertNotNull(createdGroup, "Created group is null");
        assertEquals(groupName, createdGroup.getName(), "Group name doesn't match");
        assertNotNull(createdGroup.getId(), "Group ID is null");
        assertNotNull(createdGroup.getAccount(), "Group owner is null");
        assertNotNull(createdGroup.getPosition(), "Group position is null");
        assertEquals(createdGroup.getAccount().getId(), testAccount.getId(), "Group owner doesn't match test user");

        // Get all groups of the test user and check if newly create group is first in sorting
        groups.sort(Comparator.comparingLong(GalleryGroup::getPosition));
        assertFalse(groups.isEmpty(), "No groups found for test user");
        assertEquals(groups.get(0).getId(), createdGroup.getId(), "Created group is not first in sorting");
    }

    @Test
    public void testUpdateGalleryGroup() {
        // First create a gallery group to update
        String initialName = "Test Gallery Group " + System.currentTimeMillis();
        CreateGalleryGroupRequest createRequest = new CreateGalleryGroupRequest(initialName);

        // Create the gallery group
        ResponseEntity<?> createResponse = testAuthenticatedRequest(
                baseUrl + "/gallery/groups/new?name=" + initialName,
                HttpMethod.POST,
                createRequest,
                OperationResultDTO.class);

        // Verify group was created
        assertEquals(OperationOutcome.SUCCESS,
                ((OperationResultDTO) createResponse.getBody()).getOperationOutcome());

        // Get the created group from database
        var groups = galleryGroupRepository.findAllByAccountId(testAccount.getId());
        GalleryGroup createdGroup = groups.stream()
                .filter(group -> group.getName().equals(initialName))
                .findFirst()
                .orElseThrow();

        // Setup for update test
        String updatedName = "Updated Gallery Group " + System.currentTimeMillis();
        String url = baseUrl + "/gallery/groups/" + createdGroup.getId() + "/update";
        HttpMethod method = HttpMethod.POST;
        Class<?> responseType = OperationResultDTO.class;

        // Create update request body
        UpdateGalleryRequest updateRequest = new UpdateGalleryRequest(updatedName);

        // Test unauthenticated access
        testEndpointProtection(url, method, updateRequest, responseType);

        // Test authenticated access
        ResponseEntity<?> response = testAuthenticatedRequest(url, method, updateRequest, responseType);

        // Verify response
        assertNotNull(response.getBody(), "Response body is null");
        OperationResultDTO responseBody = (OperationResultDTO) response.getBody();
        assertEquals(OperationOutcome.SUCCESS, responseBody.getOperationOutcome(),
                "Operation outcome is not success");

        // Verify database update
        GalleryGroup updatedGroup = galleryGroupRepository.findById(createdGroup.getId()).orElse(null);
        assertNotNull(updatedGroup, "Updated group is null");
        assertEquals(updatedName, updatedGroup.getName(), "Group name wasn't updated correctly");
        assertEquals(createdGroup.getAccount().getId(), updatedGroup.getAccount().getId(),
                "Group owner changed unexpectedly");
        assertEquals(createdGroup.getPosition(), updatedGroup.getPosition(),
                "Group position changed unexpectedly");
    }
}