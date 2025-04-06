package com.plainprog.grandslam_ai.object.dto.util;

public class SimpleOperationResultDTO {
    private boolean success;
    private String message;
    private String internalMessage;

    public SimpleOperationResultDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    public SimpleOperationResultDTO(boolean success, String message, String internalMessage) {
        this.success = success;
        this.message = message;
        this.internalMessage = internalMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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
