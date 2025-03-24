package com.plainprog.grandslam_ai.object.request_models.auth;

public class CreateAccountRequest {
    private String email;

    public CreateAccountRequest() {
    }

    public CreateAccountRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
