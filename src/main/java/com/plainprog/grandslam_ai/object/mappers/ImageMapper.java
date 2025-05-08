package com.plainprog.grandslam_ai.object.mappers;

import com.plainprog.grandslam_ai.entity.img_gen.Image;
import com.plainprog.grandslam_ai.object.dto.image.ImageDTO;

public class ImageMapper {
    public static ImageDTO mapToDTO(Image image) {
        if (image == null) {
            return null;
        }
        return new ImageDTO(image.getFullsize(), image.getCompressed(), image.getThumbnail());
    }
}
