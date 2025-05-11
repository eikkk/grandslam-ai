package com.plainprog.grandslam_ai.object.response_models.image_management.gallery;

import com.plainprog.grandslam_ai.object.dto.image.ImageDTO;

public class GalleryEntryUI {
    private Long id;
    private long position;
    private boolean hidden;
    private boolean shortlisted;
    private Long imageId;
    private ImageDTO image;

    // Constructors
    public GalleryEntryUI() {}

    public GalleryEntryUI(Long id, long position, boolean hidden, boolean shortlisted,
                        Long imageId, ImageDTO image) {
        this.id = id;
        this.position = position;
        this.hidden = hidden;
        this.shortlisted = shortlisted;
        this.imageId = imageId;
        this.image = image;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
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
}