package com.plainprog.grandslam_ai.object.response_models.image_management.incubator;

import com.plainprog.grandslam_ai.object.dto.image.ImageDTO;

public class IncubatorResponseItem
{
    private Long incubatorEntryId;
    private Long imageId;
    private ImageDTO image;
    private boolean shortlisted;

    public IncubatorResponseItem() {
    }
    public IncubatorResponseItem(Long incubatorEntryId, Long imageId, ImageDTO image, boolean shortlisted) {
        this.incubatorEntryId = incubatorEntryId;
        this.imageId = imageId;
        this.image = image;
        this.shortlisted = shortlisted;
    }

    public Long getIncubatorEntryId() {
        return incubatorEntryId;
    }

    public void setIncubatorEntryId(Long incubatorEntryId) {
        this.incubatorEntryId = incubatorEntryId;
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

    public boolean isShortlisted() {
        return shortlisted;
    }

    public void setShortlisted(boolean shortlisted) {
        this.shortlisted = shortlisted;
    }
}
