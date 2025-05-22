package com.plainprog.grandslam_ai.image_management.incubator;


import com.plainprog.grandslam_ai.BaseEndpointTest;
import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.img_gen.Image;
import com.plainprog.grandslam_ai.entity.img_management.GalleryEntry;
import com.plainprog.grandslam_ai.entity.img_management.GalleryEntryRepository;
import com.plainprog.grandslam_ai.entity.img_management.IncubatorEntry;
import com.plainprog.grandslam_ai.entity.img_management.IncubatorEntryRepository;
import com.plainprog.grandslam_ai.object.dto.util.OperationOutcome;
import com.plainprog.grandslam_ai.object.dto.util.OperationResultDTO;
import com.plainprog.grandslam_ai.object.request_models.other.BatchOperationOnLongIds;
import com.plainprog.grandslam_ai.object.response_models.generation.ImgGenResponse;
import com.plainprog.grandslam_ai.object.response_models.image_management.incubator.IncubatorResponseModel;
import com.plainprog.grandslam_ai.service.account.helper.TestHelper;
import com.plainprog.grandslam_ai.service.gcp.GCPStorageService;
import com.plainprog.grandslam_ai.service.generation.TestGenerationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


public class IncubatorTests extends BaseEndpointTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private IncubatorEntryRepository incubatorEntryRepository;
    @Autowired
    private GalleryEntryRepository galleryEntryRepository;
    @Autowired
    private TestGenerationHelper testGenerationHelper;
    @Autowired
    private GCPStorageService gcpStorageService;
    @Autowired
    private TestHelper testHelper;

    private List<IncubatorEntry> testEntries;
    private Account testAcc;

    @BeforeEach
    public void ensureEntriesExist() throws Exception {
        //Ensures there are at least two incubator images of test user
        testAcc = testHelper.ensureTestUserExists().getAccount();
        List<IncubatorEntry> entries = incubatorEntryRepository.findAllByImageOwnerAccountId(testAcc.getId());
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
        String url = baseUrl + "/api/incubator/batch/delete";
        HttpMethod method = HttpMethod.POST;
        Class<?> responseType = OperationResultDTO.class;

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
        for (String imgURL : allUrls) {
            assertTrue(gcpStorageService.exists(imgURL), "Image not found in GCP: " + imgURL);
        }
        //request body
        BatchOperationOnLongIds requestBody = new BatchOperationOnLongIds(imageIds);
        //make unauthenticated request
        testEndpointProtection(url, method, requestBody, responseType);
        //make authenticated request
        testAuthenticatedRequest(url, method, requestBody, responseType);

        //verify that incubator entries are deleted
        for (Long id : incubatorEntryIds) {
            assertFalse(incubatorEntryRepository.existsById(id), "Incubator entry not deleted: " + id);
        }
        //verify that images are deleted
        for (Long id : imageIds) {
            assertFalse(incubatorEntryRepository.existsById(id), "Image not deleted: " + id);
        }
        //verify that images urls are deleted from gcp
        for (String imgURL : allUrls) {
            assertFalse(gcpStorageService.exists(imgURL), "Image not deleted from GCP: " + imgURL);
        }
    }

    @Test
    public void testIncubatorPromotion(){
        String url = baseUrl + "/api/incubator/batch/promote";
        HttpMethod method = HttpMethod.POST;
        Class<?> responseType = OperationResultDTO.class;

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

        BatchOperationOnLongIds requestBody = new BatchOperationOnLongIds(imageIds);

        //make unauthenticated request
        testEndpointProtection(url, method, requestBody, responseType);
        //make authenticated request
        testAuthenticatedRequest(url, method, requestBody, responseType);

        //verify that incubator entries are deleted
        for (Long id : incubatorEntryIds) {
            assertFalse(incubatorEntryRepository.existsById(id), "Incubator entry not deleted: " + id);
        }
        //verify that gallery entries are created. We have image ids. and gallery entries have image prop with id
        List<GalleryEntry> galleryEntries = galleryEntryRepository.findAllByImageIdIn(imageIds);
        assertEquals(galleryEntries.size(), imageIds.size(), "Not all images promoted to gallery");
        for (GalleryEntry entry : galleryEntries) {
           assertNotNull(entry.getCreatedAt());
           assertNull(entry.getGroup());
        }
    }

    @Test
    void testShortlisting(){
        String url = baseUrl + "/api/incubator/batch/shortlist/";
        String urlToShortlist = url + "true";
        String urlToUnShortlist = url + "false";
        HttpMethod method = HttpMethod.POST;
        Class<?> responseType = OperationResultDTO.class;
        testEndpointProtection(urlToShortlist, method, null, responseType);

        assertTrue(testEntries.size() > 1, "Not enough entries to test shortlisting");
        List<Image> images = testEntries.stream()
                .map(IncubatorEntry::getImage)
                .toList();
        List<Long> imageIds = images.stream()
                .map(Image::getId)
                .toList();

        //make unauthenticated request
        BatchOperationOnLongIds request = new BatchOperationOnLongIds(imageIds);
        testAuthenticatedRequest(urlToShortlist, method, request, responseType);

        List<IncubatorEntry> incubatorEntries = incubatorEntryRepository.findAllByImageIdIn(imageIds);
        for (IncubatorEntry entry : incubatorEntries) {
            assertTrue(entry.isShortlisted(), "Incubator entry not shortlisted: " + entry.getId());
        }

        //test removing shortlist
        testAuthenticatedRequest(urlToUnShortlist, method, request, responseType);

        incubatorEntries = incubatorEntryRepository.findAllByImageIdIn(imageIds);
        for (IncubatorEntry entry : incubatorEntries) {
            assertFalse(entry.isShortlisted(), "Incubator entry not shortlisted: " + entry.getId());
        }
    }

    @Test
    void testIncubatorGetAll() throws Exception {
        String url = baseUrl + "/api/incubator/all";
        HttpMethod method = HttpMethod.GET;
        Class<?> responseType = IncubatorResponseModel.class;

        testEndpointProtection(url, method, null, responseType);
        // Now let's log in the user
        ResponseEntity<?> response = testAuthenticatedRequest(url, method, null, responseType);
        IncubatorResponseModel responseBody = (IncubatorResponseModel)response.getBody();

        assertNotNull(responseBody, "Response body is null");
        assertEquals(OperationOutcome.SUCCESS, responseBody.getOperationResult().getOperationOutcome(), "Operation failed");
        assertFalse(responseBody.getEntries().isEmpty(), "No incubator entries found");
    }
}
