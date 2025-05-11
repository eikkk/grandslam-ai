package com.plainprog.grandslam_ai.object.request_models.gallery;

public class UpdateGalleryRequest {
    private String newName;

    public UpdateGalleryRequest() {
    }
    public UpdateGalleryRequest(String newName) {
        this.newName = newName;
    }
    public String getNewName() {
        return newName;
    }
    public void setNewName(String newName) {
        this.newName = newName;
    }
}
