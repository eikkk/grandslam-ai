package com.plainprog.grandslam_ai.object.dto.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OperationResultDTO {

    @JsonProperty("operationOutcome")
    private int operationOutcome;

    @JsonProperty("message")
    private String message;

    @JsonProperty("internalMessage")
    private String internalMessage;

    public OperationResultDTO(int operationOutcome, String message, String internalMessage) {
        this.operationOutcome = operationOutcome;
        this.message = message;
        this.internalMessage = internalMessage;
    }

    // Getters and setters
    public int getOperationOutcome() {
        return operationOutcome;
    }

    public void setOperationOutcome(int operationOutcome) {
        this.operationOutcome = operationOutcome;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInternalMessage() {
        return internalMessage;
    }

    public void setInternalMessage(String internalMessage) {
        this.internalMessage = internalMessage;
    }
}