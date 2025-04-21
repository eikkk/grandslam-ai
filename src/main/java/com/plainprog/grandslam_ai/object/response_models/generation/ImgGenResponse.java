package com.plainprog.grandslam_ai.object.response_models.generation;

import com.plainprog.grandslam_ai.object.dto.image.ImageDTO;

public class ImgGenResponse {
    private int imageId;
    private ImageDTO image;
    private int seed;

    public ImgGenResponse(int imageId, ImageDTO image, int seed) {
        this.imageId = imageId;
        this.image = image;
        this.seed = seed;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public ImageDTO getImage() {
        return image;
    }

    public void setImage(ImageDTO image) {
        this.image = image;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }
}
