package com.plainprog.grandslam_ai.object.response_models.image_management.incubator;

import com.plainprog.grandslam_ai.object.dto.util.OperationResultDTO;

import java.util.List;

public class IncubatorResponseModel {
    private List<IncubatorResponseItem> entries;
    private OperationResultDTO operationResult;

    public IncubatorResponseModel() {
    }
    public IncubatorResponseModel(List<IncubatorResponseItem> entries) {
        this.entries = entries;
    }
    public IncubatorResponseModel(List<IncubatorResponseItem> entries, OperationResultDTO operationResult) {
        this.entries = entries;
        this.operationResult = operationResult;
    }

    public OperationResultDTO getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(OperationResultDTO operationResult) {
        this.operationResult = operationResult;
    }

    public List<IncubatorResponseItem> getEntries() {
        return entries;
    }

    public void setEntries(List<IncubatorResponseItem> entries) {
        this.entries = entries;
    }

    public static IncubatorResponseModel empty() {
        return new IncubatorResponseModel(List.of());
    }

}
