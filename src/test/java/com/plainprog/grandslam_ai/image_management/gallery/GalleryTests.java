package com.plainprog.grandslam_ai.image_management.gallery;

import com.plainprog.grandslam_ai.BaseEndpointTest;
import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.img_management.GalleryGroup;
import com.plainprog.grandslam_ai.entity.img_management.GalleryGroupRepository;
import com.plainprog.grandslam_ai.object.dto.util.OperationOutcome;
import com.plainprog.grandslam_ai.object.dto.util.OperationResultDTO;
import com.plainprog.grandslam_ai.object.request_models.gallery.CreateGalleryGroupRequest;
import com.plainprog.grandslam_ai.object.request_models.gallery.GroupsChangeOrderRequest;
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

    @Test
    public void testChangeGalleryGroupOrder() {
        // Check if we have at least 3 gallery groups, create if needed
        var existingGroups = galleryGroupRepository.findAllByAccountId(testAccount.getId());
        while (existingGroups.size() < 3) {
            String groupName = "Order Test Group " + System.currentTimeMillis() + existingGroups.size();
            CreateGalleryGroupRequest createRequest = new CreateGalleryGroupRequest(groupName);

            // Create additional gallery group
            testAuthenticatedRequest(
                    baseUrl + "/gallery/groups/new?name=" + groupName,
                    HttpMethod.POST,
                    createRequest,
                    OperationResultDTO.class);

            // Refresh the list of groups
            existingGroups = galleryGroupRepository.findAllByAccountId(testAccount.getId());
        }

        // Sort groups by position to understand their current order
        existingGroups.sort(Comparator.comparingLong(GalleryGroup::getPosition));

        // Select three groups to work with
        GalleryGroup firstGroup = existingGroups.get(0);
        GalleryGroup secondGroup = existingGroups.get(1);
        GalleryGroup thirdGroup = existingGroups.get(2);

        // Define the endpoint URL
        String orderUrl = baseUrl + "/gallery/groups/{id}/order";
        HttpMethod method = HttpMethod.POST;
        Class<?> responseType = OperationResultDTO.class;

        // Test 1: Place the first group after the third group
        GroupsChangeOrderRequest request1 = new GroupsChangeOrderRequest();
        request1.setPlaceAfterGroupId(thirdGroup.getId());

        // Test unauthenticated access
        testEndpointProtection(orderUrl.replace("{id}", firstGroup.getId().toString()),
                method, request1, responseType);

        // Test authenticated access
        ResponseEntity<?> response1 = testAuthenticatedRequest(
                orderUrl.replace("{id}", firstGroup.getId().toString()),
                method, request1, responseType);

        // Verify response
        assertNotNull(response1.getBody(), "Response body is null");
        OperationResultDTO responseBody1 = (OperationResultDTO) response1.getBody();
        assertEquals(OperationOutcome.SUCCESS, responseBody1.getOperationOutcome(),
                "Operation outcome is not success");

        // Verify database update - group order should now be: second, third, first
        var updatedGroups = galleryGroupRepository.findAllByAccountId(testAccount.getId());
        updatedGroups.sort(Comparator.comparingLong(GalleryGroup::getPosition));

        // Find our test groups in the updated list
        var updatedFirst = updatedGroups.stream()
                .filter(g -> g.getId().equals(firstGroup.getId()))
                .findFirst().orElseThrow();
        var updatedSecond = updatedGroups.stream()
                .filter(g -> g.getId().equals(secondGroup.getId()))
                .findFirst().orElseThrow();
        var updatedThird = updatedGroups.stream()
                .filter(g -> g.getId().equals(thirdGroup.getId()))
                .findFirst().orElseThrow();

        // Verify the positions reflect the requested change
        assertTrue(updatedSecond.getPosition() < updatedThird.getPosition(),
                "Second group should be before third group");
        assertTrue(updatedThird.getPosition() < updatedFirst.getPosition(),
                "Third group should be before first group (which was moved to the end)");

        // Test 2: Now place the first group before the second group
        GroupsChangeOrderRequest request2 = new GroupsChangeOrderRequest();
        request2.setPlaceBeforeGroupId(secondGroup.getId());

        ResponseEntity<?> response2 = testAuthenticatedRequest(
                orderUrl.replace("{id}", firstGroup.getId().toString()),
                method, request2, responseType);

        // Verify response
        assertNotNull(response2.getBody(), "Response body is null");
        OperationResultDTO responseBody2 = (OperationResultDTO) response2.getBody();
        assertEquals(OperationOutcome.SUCCESS, responseBody2.getOperationOutcome(),
                "Operation outcome is not success");

        // Verify database update - group order should now be: first, second, third
        updatedGroups = galleryGroupRepository.findAllByAccountId(testAccount.getId());
        updatedGroups.sort(Comparator.comparingLong(GalleryGroup::getPosition));

        // Find our test groups in the updated list again
        updatedFirst = updatedGroups.stream()
                .filter(g -> g.getId().equals(firstGroup.getId()))
                .findFirst().orElseThrow();
        updatedSecond = updatedGroups.stream()
                .filter(g -> g.getId().equals(secondGroup.getId()))
                .findFirst().orElseThrow();
        updatedThird = updatedGroups.stream()
                .filter(g -> g.getId().equals(thirdGroup.getId()))
                .findFirst().orElseThrow();

        // Verify the positions reflect the requested change
        assertTrue(updatedFirst.getPosition() < updatedSecond.getPosition(),
                "First group should be before second group");
        assertTrue(updatedSecond.getPosition() < updatedThird.getPosition(),
                "Second group should be before third group");
    }
}