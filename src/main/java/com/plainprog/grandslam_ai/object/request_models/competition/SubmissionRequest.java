package com.plainprog.grandslam_ai.object.request_models.competition;

public class SubmissionRequest {
    private Long galleryEntryId;
    private Long competitionId;

    public SubmissionRequest() {
    }

    public Long getGalleryEntryId() {
        return galleryEntryId;
    }

    public void setGalleryEntryId(Long galleryEntryId) {
        this.galleryEntryId = galleryEntryId;
    }

    public Long getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(Long competitionId) {
        this.competitionId = competitionId;
    }
}
