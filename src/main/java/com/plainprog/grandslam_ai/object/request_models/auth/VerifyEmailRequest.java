package com.plainprog.grandslam_ai.object.request_models.auth;

public class VerifyEmailRequest {
    private String email;
    private String token;

    public VerifyEmailRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
