package com.plainprog.grandslam_ai.object.dto.image;

public class ImageDTO {
    private String fullSize;
    private String compressed;
    private String thumbnail;

    public ImageDTO(String fullSize, String compressed, String thumbnail) {
        this.fullSize = fullSize;
        this.compressed = compressed;
        this.thumbnail = thumbnail;
    }

    public String getFullSize() {
        return fullSize;
    }

    public void setFullSize(String fullSize) {
        this.fullSize = fullSize;
    }

    public String getCompressed() {
        return compressed;
    }

    public void setCompressed(String compressed) {
        this.compressed = compressed;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
