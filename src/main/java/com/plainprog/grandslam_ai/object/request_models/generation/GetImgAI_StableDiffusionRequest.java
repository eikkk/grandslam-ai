package com.plainprog.grandslam_ai.object.request_models.generation;


public class GetImgAI_StableDiffusionRequest {
    private String model;
    private String prompt;
    private String negative_prompt;
    private String response_format;
    private String output_format;
    private int steps;
    private int width;
    private int height;
    private Integer seed;

    public GetImgAI_StableDiffusionRequest() {
    }

    public GetImgAI_StableDiffusionRequest(String model, String prompt, String negative_prompt, String response_format, String output_format, Integer steps, int width, int height) {
        this.model = model;
        this.prompt = prompt;
        this.negative_prompt = negative_prompt;
        this.response_format = response_format;
        this.output_format = output_format;
        this.steps = steps;
        this.width = width;
        this.height = height;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getNegative_prompt() {
        return negative_prompt;
    }

    public void setNegative_prompt(String negative_prompt) {
        this.negative_prompt = negative_prompt;
    }

    public String getResponse_format() {
        return response_format;
    }

    public void setResponse_format(String response_format) {
        this.response_format = response_format;
    }

    public String getOutput_format() {
        return output_format;
    }

    public void setOutput_format(String output_format) {
        this.output_format = output_format;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Integer getSeed() {
        return seed;
    }

    public void setSeed(Integer seed) {
        this.seed = seed;
    }
}
