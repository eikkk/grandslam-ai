package com.plainprog.grandslam_ai.object.request_models.gallery;

import java.util.List;

public class ReorderGalleryItemsRequest {
    private List<Long> entryIds;

    public ReorderGalleryItemsRequest() {
    }
    public ReorderGalleryItemsRequest(List<Long> entryIds) {
        this.entryIds = entryIds;
    }
    public List<Long> getEntryIds() {
        return entryIds;
    }
    public void setEntryIds(List<Long> entryIds) {
        this.entryIds = entryIds;
    }
}