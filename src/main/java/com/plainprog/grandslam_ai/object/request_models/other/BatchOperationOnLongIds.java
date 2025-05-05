package com.plainprog.grandslam_ai.object.request_models.other;

import java.util.List;

public class BatchOperationOnLongIds {
    private List<Long> ids;

    public BatchOperationOnLongIds() {
    }
    public BatchOperationOnLongIds(List<Long> ids) {
        this.ids = ids;
    }
    public List<Long> getIds() {
        return ids;
    }
    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
