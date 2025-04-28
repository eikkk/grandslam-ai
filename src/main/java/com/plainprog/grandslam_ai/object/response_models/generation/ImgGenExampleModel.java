package com.plainprog.grandslam_ai.object.response_models.generation;

import com.plainprog.grandslam_ai.object.dto.image.ImageDTO;

public class ImgGenExampleModel {
    private ImageDTO image;
    private String prompt;

    public ImgGenExampleModel(ImageDTO image, String prompt) {
        this.image = image;
        this.prompt = prompt;
    }

    public ImageDTO getImage() {
        return image;
    }

    public void setImage(ImageDTO image) {
        this.image = image;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
