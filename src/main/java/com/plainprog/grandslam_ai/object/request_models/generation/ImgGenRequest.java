package com.plainprog.grandslam_ai.object.request_models.generation;


public class ImgGenRequest {
    private String prompt;
    private String negativePrompt;
    private String orientation; // "v" or "h" or "s"
    private int providerId;
    private int moduleId;
    private Integer seed;
    private Integer steps;


    public ImgGenRequest() {
    }

    public ImgGenRequest(String prompt, String orientation, int providerId, int moduleId) {
        this.prompt = prompt;
        this.orientation = orientation;
        this.providerId = providerId;
        this.moduleId = moduleId;
    }

    public ImgGenRequest(String prompt, String negativePrompt, String orientation, int providerId, int moduleId, int seed, Integer steps) {
        this.prompt = prompt;
        this.negativePrompt = negativePrompt;
        this.orientation = orientation;
        this.providerId = providerId;
        this.moduleId = moduleId;
        this.seed = seed;
        this.steps = steps;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public String getNegativePrompt() {
        return negativePrompt;
    }

    public void setNegativePrompt(String negativePrompt) {
        this.negativePrompt = negativePrompt;
    }

    public Integer getSeed() {
        return seed;
    }

    public void setSeed(Integer seed) {
        this.seed = seed;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }
}
