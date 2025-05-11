package com.plainprog.grandslam_ai.object.request_models.gallery;

import com.plainprog.grandslam_ai.object.request_models.other.BatchOperationOnLongIds;

public class BatchMoveGalleryItemsRequest extends BatchOperationOnLongIds {
    private Integer targetGroupId; // Can be null when moving to no group

    public BatchMoveGalleryItemsRequest() {
    }

    public Integer getTargetGroupId() {
        return targetGroupId;
    }

    public void setTargetGroupId(Integer targetGroupId) {
        this.targetGroupId = targetGroupId;
    }
}