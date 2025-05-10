package com.plainprog.grandslam_ai.image_management.gallery;

import com.plainprog.grandslam_ai.BaseEndpointTest;
import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.img_management.GalleryGroup;
import com.plainprog.grandslam_ai.entity.img_management.GalleryGroupRepository;
import com.plainprog.grandslam_ai.object.dto.util.OperationResultDTO;
import com.plainprog.grandslam_ai.object.request_models.gallery.CreateGalleryGroupRequest;
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
        CreateGalleryGroupRequest request = new CreateGalleryGroupRequest(groupName);

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
}