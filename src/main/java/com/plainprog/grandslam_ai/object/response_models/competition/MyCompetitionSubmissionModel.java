package com.plainprog.grandslam_ai.object.response_models.competition;

import com.plainprog.grandslam_ai.object.dto.image.ImageDTO;

public class MyCompetitionSubmissionModel {
    private long submissionId;
    private long imageId;
    private ImageDTO image;

    public MyCompetitionSubmissionModel(long submissionId, long imageId, ImageDTO image) {
        this.submissionId = submissionId;
        this.imageId = imageId;
        this.image = image;
    }

    public long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(long submissionId) {
        this.submissionId = submissionId;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public ImageDTO getImage() {
        return image;
    }

    public void setImage(ImageDTO image) {
        this.image = image;
    }
}
