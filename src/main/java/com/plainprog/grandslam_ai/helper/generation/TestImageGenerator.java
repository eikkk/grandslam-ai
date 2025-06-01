package com.plainprog.grandslam_ai.helper.generation;

import com.plainprog.grandslam_ai.object.dto.image.ImgGenCommonResult;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Helper class for generating test images instead of calling real image generation APIs
 */
@Component
public class TestImageGenerator {
    
    /**
     * Generates a simple one-color test image
     * 
     * @param width The width of the image
     * @param height The height of the image
     * @return ImgGenCommonResult containing the base64 encoded image data
     * @throws IOException if image creation fails
     */
    public ImgGenCommonResult generateTestImage(int width, int height) throws IOException {
        // Create a simple colored image
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        
        // Generate a random pastel color
        Random random = new Random();
        Color color = new Color(
            128 + random.nextInt(128),
            128 + random.nextInt(128),
            128 + random.nextInt(128)
        );
        
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        g.dispose();
        
        // Convert to base64 string
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(img, "jpeg", os);
        
        // For test mode, we'll just use a random seed and zero price
        return new ImgGenCommonResult(
            "data:image/jpeg;base64," + java.util.Base64.getEncoder().encodeToString(os.toByteArray()),
            0.0,
            new Random().nextInt(1000000)
        );
    }
}
