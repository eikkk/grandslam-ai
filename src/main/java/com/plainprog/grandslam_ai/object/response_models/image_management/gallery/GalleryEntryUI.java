package com.plainprog.grandslam_ai.object.response_models.image_management.gallery;

import com.plainprog.grandslam_ai.object.dto.image.ImageDTO;

import java.time.Instant;

public class GalleryEntryUI {
    private Long imageId;
    private Long entryId;
    private long position;
    private Instant hiddenAt;
    private boolean shortlisted;
    private boolean spotlighted;
    private ImageDTO image;

    // Constructors
    public GalleryEntryUI() {}

    public GalleryEntryUI(Long entryId, long position, Instant hiddenAt, boolean shortlisted,
                          Long imageId, ImageDTO image, boolean spotlighted) {
        this.entryId = entryId;
        this.position = position;
        this.hiddenAt = hiddenAt;
        this.shortlisted = shortlisted;
        this.imageId = imageId;
        this.image = image;
        this.spotlighted = spotlighted;
    }

    // Getters and setters
    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public Instant getHiddenAt() {
        return hiddenAt;
    }

    public void setHiddenAt(Instant hiddenAt) {
        this.hiddenAt = hiddenAt;
    }

    public boolean isShortlisted() {
        return shortlisted;
    }

    public void setShortlisted(boolean shortlisted) {
        this.shortlisted = shortlisted;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }
    public ImageDTO getImage() {
        return image;
    }
    public void setImage(ImageDTO image) {
        this.image = image;
    }
    public boolean isSpotlighted() {
        return spotlighted;
    }
    public void setSpotlighted(boolean spotlighted) {
        this.spotlighted = spotlighted;
    }
}