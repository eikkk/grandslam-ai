package com.plainprog.grandslam_ai.object.request_models.generation;

public class SeedRegenRequest {
    private String prompt;

    public SeedRegenRequest() {
    }

    public SeedRegenRequest(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
