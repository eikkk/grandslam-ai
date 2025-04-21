package com.plainprog.grandslam_ai.object.dto.image;

public class ImgGenCommonResult {
    private String url;
    private double price;
    private int seed;

    public ImgGenCommonResult() {
    }

    public ImgGenCommonResult(String url, double price, int seed) {
        this.url = url;
        this.price = price;
        this.seed = seed;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }
}

