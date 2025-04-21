package com.plainprog.grandslam_ai.helper.image;

import com.plainprog.grandslam_ai.object.dto.util.Size;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageCompressor {
    public enum CompressType {
        NONE, Q, T
    }
    public ImageCompressor() {
    }

    public BufferedImage compressWithQuality(BufferedImage img, Size size) throws IOException {
        return Scalr.resize(img, Scalr.Method.ULTRA_QUALITY, size.getWidth(), size.getHeight());
    }

    public BufferedImage compressAutomatic(BufferedImage img, Size size) throws IOException {
        return Scalr.resize(img, Scalr.Method.AUTOMATIC, size.getWidth(), size.getHeight());
    }
    public static InputStream stream(BufferedImage image) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        os.close();
        return is;
    }
}
