package com.plainprog.grandslam_ai.object.request_models.generation;


public class ImgGenRequest {
    private String prompt;
    private String negativePrompt;
    private String orientation; // "v" or "h" or "s"
    private int moduleId;
    private Integer steps;


    public ImgGenRequest() {
    }

    public ImgGenRequest(String prompt, String orientation, int providerId, int moduleId) {
        this.prompt = prompt;
        this.orientation = orientation;
        this.moduleId = moduleId;
    }

    public ImgGenRequest(String prompt, String negativePrompt, String orientation, int providerId, int moduleId, int seed, Integer steps) {
        this.prompt = prompt;
        this.negativePrompt = negativePrompt;
        this.orientation = orientation;
        this.moduleId = moduleId;
        this.steps = steps;
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

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }
}
