package com.plainprog.grandslam_ai.object.request_models.gallery;

public class CreateGalleryGroupRequest {
    private String name;

    public CreateGalleryGroupRequest() {
    }
    public CreateGalleryGroupRequest(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
