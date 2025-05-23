package com.plainprog.grandslam_ai.object.response_models.generation;

import com.plainprog.grandslam_ai.object.dto.image.ImageDTO;
import com.plainprog.grandslam_ai.object.dto.util.OperationResultDTO;

public class ImgGenResponse {
    private long imageId;
    private ImageDTO image;
    private int seed;
    private OperationResultDTO operationResult;

    public ImgGenResponse(long imageId, ImageDTO image, int seed) {
        this.imageId = imageId;
        this.image = image;
        this.seed = seed;
    }

    public ImgGenResponse() {
    }

    public long getImageId() {
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

    public OperationResultDTO getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(OperationResultDTO operationResult) {
        this.operationResult = operationResult;
    }
}
