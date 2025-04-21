package com.plainprog.grandslam_ai.generation;

import com.plainprog.grandslam_ai.config.TestConfig;
import com.plainprog.grandslam_ai.helper.generation.Prompts;
import com.plainprog.grandslam_ai.object.constant.images.ImgGenModuleId;
import com.plainprog.grandslam_ai.object.constant.images.ProviderId;
import com.plainprog.grandslam_ai.object.dto.util.Size;
import com.plainprog.grandslam_ai.object.request_models.generation.ImgGenRequest;
import com.plainprog.grandslam_ai.object.response_models.generation.ImgGenResponse;
import com.plainprog.grandslam_ai.service.account.helper.TestUserHelper;
import com.plainprog.grandslam_ai.service.generation.ImageGenerationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {TestConfig.class})
public class ImageGenerationTest {
    @Value("${app.url.base}")
    private String baseUrl;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TestUserHelper testUserHelper;

    @Test
    public void imageGenerationEndpointTest() throws Exception {
        //Given
        ImgGenRequest request = new ImgGenRequest(Prompts.testPrompt, "s", ProviderId.STABLE_DIFFUSION_XL, ImgGenModuleId.RAW_STABLE_DIFFUSION_XL);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<ImgGenRequest> entity = new HttpEntity<>(request, headers);
        //Test that endpoint is protected by authentication
        ResponseEntity<ImgGenResponse> responseEntity =
                restTemplate.postForEntity(baseUrl + "/api/gen/image", entity, ImgGenResponse.class);

        //Request not allowed
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());


        // Log in the user
        HttpHeaders headersWithAuth = testUserHelper.initiateSession();
        assertTrue(headersWithAuth.containsKey("Cookie"));

        // Now we can access the endpoint with our headers
        headersWithAuth.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<ImgGenRequest> entityWithAuth = new HttpEntity<>(request, headersWithAuth);
        responseEntity =
                restTemplate.postForEntity(baseUrl + "/api/gen/image", entityWithAuth, ImgGenResponse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        //Check that the response is not null and contains the expected data
        ImgGenResponse response = responseEntity.getBody();
        assert response != null;
        assertFalse(response.getImage().getFullSize().isEmpty());
        assertFalse(response.getImage().getThumbnail().isEmpty());
        assertFalse(response.getImage().getCompressed().isEmpty());

        // Validate images
        validateImage(response.getImage().getFullSize(), 50, 500, ImageGenerationService.BASE_SIZE, ImageGenerationService.BASE_SIZE);
        int sizeCompressed = (int)(ImageGenerationService.BASE_SIZE * ImageGenerationService.Q_COMPRESSION);
        validateImage(response.getImage().getCompressed(), 20, 100, sizeCompressed, sizeCompressed);
        int sizeThumbnail = (int)(ImageGenerationService.BASE_SIZE * ImageGenerationService.T_COMPRESSION);
        validateImage(response.getImage().getThumbnail(), 1, 25, sizeThumbnail, sizeThumbnail);
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
            assertTrue(sizeInKB > minSizeKB && sizeInKB < maxSizeKB, "Image size is not in the expected range: " + sizeInKB);
            assertEquals(expectedWidth, bufferedImage.getWidth(), "Image width is not as expected");
            assertEquals(expectedHeight, bufferedImage.getHeight(), "Image height is not as expected");
        });
    }
}
