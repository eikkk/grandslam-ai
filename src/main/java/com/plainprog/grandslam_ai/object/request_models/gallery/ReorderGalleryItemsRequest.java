package com.plainprog.grandslam_ai.object.request_models.gallery;

import java.util.List;

public class ReorderGalleryItemsRequest {
    private List<Long> itemIds;

    public List<Long> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Long> itemIds) {
        this.itemIds = itemIds;
    }
}