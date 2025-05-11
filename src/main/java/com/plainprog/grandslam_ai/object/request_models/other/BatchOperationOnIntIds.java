package com.plainprog.grandslam_ai.object.request_models.other;

public class BatchOperationOnIntIds {
    private Integer[] ids;

    public BatchOperationOnIntIds() {
    }

    public BatchOperationOnIntIds(Integer[] ids) {
        this.ids = ids;
    }

    public Integer[] getIds() {
        return ids;
    }

    public void setIds(Integer[] ids) {
        this.ids = ids;
    }
}
