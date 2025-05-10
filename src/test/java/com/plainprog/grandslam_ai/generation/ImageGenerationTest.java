package com.plainprog.grandslam_ai.generation;

import com.plainprog.grandslam_ai.BaseEndpointTest;
import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.img_gen.Image;
import com.plainprog.grandslam_ai.entity.img_gen.ImageRepository;
import com.plainprog.grandslam_ai.entity.img_management.IncubatorEntry;
import com.plainprog.grandslam_ai.entity.img_management.IncubatorEntryRepository;
import com.plainprog.grandslam_ai.helper.generation.Prompts;
import com.plainprog.grandslam_ai.object.constant.images.ImgGenModuleId;
import com.plainprog.grandslam_ai.object.constant.images.ProviderId;
import com.plainprog.grandslam_ai.object.request_models.generation.ImgGenRequest;
import com.plainprog.grandslam_ai.object.request_models.generation.SeedRegenRequest;
import com.plainprog.grandslam_ai.object.response_models.generation.ImgGenResponse;
import com.plainprog.grandslam_ai.service.account.helper.TestUserHelper;
import com.plainprog.grandslam_ai.service.generation.ImageGenerationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ImageGenerationTest extends BaseEndpointTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TestUserHelper testUserHelper;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private IncubatorEntryRepository incubatorEntryRepository;

    @Test
    public void imageGenerationEndpointTest() throws Exception {
        String url = baseUrl + "/api/gen/image";
        HttpMethod method = HttpMethod.POST;
        Class<?> responseType = ImgGenResponse.class;

        //Form request
        String negativePrompt = Prompts.negativePrompt(ImgGenModuleId.AESTHETIC);
        ImgGenRequest request = new ImgGenRequest(Prompts.testPrompt , "s", ProviderId.STABLE_DIFFUSION_XL, ImgGenModuleId.AESTHETIC);
        request.setNegativePrompt(negativePrompt);

        //Test that endpoint is protected by authentication
        testEndpointProtection(url, method, request, responseType);
        //Test authenticated request
        ResponseEntity<?> response = testAuthenticatedRequest(url, method, request, responseType);
        //Check that the response is not null and contains the expected data
        ImgGenResponse responseBody = (ImgGenResponse)response.getBody();
        assert responseBody != null;
        imageResultValidation(responseBody);
}

    @Test
    public void imageRegenerationTest() throws Exception {
        HttpMethod method = HttpMethod.POST;
        Class<?> responseType = ImgGenResponse.class;
        Account acc = testUserHelper.ensureTestUserExists();
        //Given
        Optional<Image> testImage = imageRepository.findFirstByOwnerAccountId(acc.getId());
        Optional<Image> imageOfOtherOwner = imageRepository.findFirstByOwnerAccountIdNot(acc.getId());

        assertTrue(testImage.isPresent());
        assertTrue(imageOfOtherOwner.isPresent());
        assertNotNull(testImage.get().getPrompt());
        assertNotNull(imageOfOtherOwner.get().getNegativePrompt());
        assertNotNull(imageOfOtherOwner.get().getPrompt());

        String url = baseUrl + "/api/gen/image/" + testImage.get().getId() + "/regen";

        //test that endpoint is protected by authentication
        testEndpointProtection(url, method, null, responseType);
        //Test authenticated request
        ResponseEntity<?> response = testAuthenticatedRequest(url, method, null, responseType);
        ImgGenResponse responseBody = (ImgGenResponse)response.getBody();
        assert responseBody != null;
        imageResultValidation(responseBody);

        var imageDB = imageRepository.findById(responseBody.getImageId()).orElse(null);
        assertNotNull(imageDB, "Image should exist in the database");
        assertEquals(imageDB.getOwnerAccount().getId(), testUserHelper.ensureTestUserExists().getId(), "Wrong image owner");

        //Try to request regeneration of image of other owner
        String urlOtherOwner = baseUrl + "/api/gen/image/" + imageOfOtherOwner.get().getId() + "/regen";
        testForPositiveExpectedError(urlOtherOwner, method, null, responseType);
    }

    @Test
    public void seedGenerationTest() throws Exception {
        String urlTemplate = baseUrl + "/api/gen/image/%d/seed-regen";
        HttpMethod method = HttpMethod.POST;
        Class<?> responseType = ImgGenResponse.class;

        Account acc = testUserHelper.ensureTestUserExists();
        Optional<Image> testImage = imageRepository.findFirstByOwnerAccountId(acc.getId());
        Optional<Image> imageOfOtherOwner = imageRepository.findFirstByOwnerAccountIdNot(acc.getId());

        assertTrue(testImage.isPresent(), "Test image not found");
        assertTrue(imageOfOtherOwner.isPresent(), "Image of other owner not found");
        assertNotNull(testImage.get().getPrompt(), "Test image prompt is null");
        assertNotNull(imageOfOtherOwner.get().getNegativePrompt(), "Other owner's image negative prompt is null");
        assertNotNull(imageOfOtherOwner.get().getPrompt(), "Other owner's image prompt is null");

        // Form request
        SeedRegenRequest request = new SeedRegenRequest(testImage.get().getPrompt());

        // Test that endpoint is protected by authentication
        String url = String.format(urlTemplate, testImage.get().getId());
        testEndpointProtection(url, method, request, responseType);

        // Test authenticated request
        ResponseEntity<?> response = testAuthenticatedRequest(url, method, request, responseType);
        ImgGenResponse responseBody = (ImgGenResponse) response.getBody();
        assertNotNull(responseBody, "Response body is null");
        imageResultValidation(responseBody);

        // Validate image in the database
        Image imageDB = imageRepository.findById(responseBody.getImageId()).orElse(null);
        assertNotNull(imageDB, "Image should exist in the database");
        assertEquals(testImage.get().getSeed(), imageDB.getSeed(), "Seed should match the original image");
        assertEquals(imageDB.getOwnerAccount().getId(), acc.getId(), "Wrong image owner");

        // Try to request regeneration of image of another owner
        String urlOtherOwner = String.format(urlTemplate, imageOfOtherOwner.get().getId());
        SeedRegenRequest requestOtherOwner = new SeedRegenRequest(imageOfOtherOwner.get().getPrompt());
        testForPositiveExpectedError(urlOtherOwner, method, requestOtherOwner, responseType);
    }
    private void imageResultValidation(ImgGenResponse response) throws Exception {
        assertFalse(response.getImage().getFullSize().isEmpty());
        assertFalse(response.getImage().getThumbnail().isEmpty());
        assertFalse(response.getImage().getCompressed().isEmpty());

        // Validate images
        validateImage(response.getImage().getFullSize(), 50, 500, ImageGenerationService.BASE_SIZE, ImageGenerationService.BASE_SIZE);
        int sizeCompressed = (int)(ImageGenerationService.BASE_SIZE * ImageGenerationService.Q_COMPRESSION);
        validateImage(response.getImage().getCompressed(), 20, 100, sizeCompressed, sizeCompressed);
        int sizeThumbnail = (int)(ImageGenerationService.BASE_SIZE * ImageGenerationService.T_COMPRESSION);
        validateImage(response.getImage().getThumbnail(), 1, 30, sizeThumbnail, sizeThumbnail);

        // Fetch image from DB and validate all the columns not empty
        long imageId = response.getImageId();
        assertTrue(imageId > 0, "Image ID should be greater than 0");
        Image imageDB = imageRepository.findById(imageId).orElse(null);
        assertNotNull(imageDB, "Image should exist in the database");
        assertNotNull(imageDB.getFullsize(), "Full size image URL should not be null");
        assertNotNull(imageDB.getCompressed(), "Compressed image URL should not be null");
        assertNotNull(imageDB.getThumbnail(), "Thumbnail image URL should not be null");
        assertNotNull(imageDB.getOrientation(), "Image orientation should not be null");
        assertNotNull(imageDB.getSeed(), "Image seed should not be null");
        assertNotNull(imageDB.getCreatedAt(), "Image creation time should not be null");
        assertNotNull(imageDB.getPrompt(), "Image prompt should not be null");
        assertNotNull(imageDB.getNegativePrompt(), "Image negative prompt should not be null");
        assertNotNull(imageDB.getSeed(), "Image generation module should not be null");
        assertNotNull(imageDB.getSteps(), "Image generation module should not be null");
        assertEquals(imageDB.getOwnerAccount().getId(), testUserHelper.ensureTestUserExists().getId(), "Wrong image owner");

        // Validate incubator entry
        IncubatorEntry incubatorEntry = incubatorEntryRepository.findByImageId(imageId);
        assertNotNull(incubatorEntry, "Incubator entry should exist in the database");
        assertFalse(incubatorEntry.isShortlisted(), "Incubator entry should not be shortlisted by default");
    }
    private void validateImage(String imageUrl, double minSizeKB, double maxSizeKB, int expectedWidth, int expectedHeight) throws Exception {
        ResponseEntity<byte[]> imageResponse = restTemplate.getForEntity(imageUrl, byte[].class);
        assertEquals(HttpStatus.OK, imageResponse.getStatusCode());

        byte[] imageBytes = imageResponse.getBody();
        assertNotNull(imageBytes);
        assertTrue(imageBytes.length > 0);

        assertDoesNotThrow(() -> {
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
            assertNotNull(bufferedImage);

            double sizeInKB = (double) imageBytes.length / 1024;
            assertTrue(sizeInKB > minSizeKB && sizeInKB < maxSizeKB, "Image size is not in the expected range: " + sizeInKB + "Expected: " + minSizeKB + " - " + maxSizeKB);
            assertEquals(expectedWidth, bufferedImage.getWidth(), "Image width is not as expected");
            assertEquals(expectedHeight, bufferedImage.getHeight(), "Image height is not as expected");
        });
    }
}
