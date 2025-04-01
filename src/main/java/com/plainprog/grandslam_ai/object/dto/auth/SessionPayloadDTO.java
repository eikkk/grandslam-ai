package com.plainprog.grandslam_ai.object.dto.auth;

public class SessionPayloadDTO {
    private String email;

    public SessionPayloadDTO() {
    }

    public SessionPayloadDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
