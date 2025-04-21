package com.plainprog.grandslam_ai.object.response_models.generation;


public class GetImgAI_StableDiffusionResponse {
    private String url;
    private double cost;
    private int seed;

    public GetImgAI_StableDiffusionResponse() {
    }

    public GetImgAI_StableDiffusionResponse(String url, double cost) {
        this.url = url;
        this.cost = cost;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }
}
