package com.plainprog.grandslam_ai.object.dto.competition;

import com.plainprog.grandslam_ai.entity.img_management.GalleryEntry;

public class ImageCompetitionComplianceDTO {
    private GalleryEntry galleryEntry;
    private boolean compliant;
    private String reason;

    public ImageCompetitionComplianceDTO() {
    }

    public ImageCompetitionComplianceDTO(GalleryEntry galleryEntry, boolean compliant, String reason) {
        this.galleryEntry = galleryEntry;
        this.compliant = compliant;
        this.reason = reason;
    }

    public GalleryEntry getGalleryEntry() {
        return galleryEntry;
    }

    public void setGalleryEntry(GalleryEntry galleryEntry) {
        this.galleryEntry = galleryEntry;
    }

    public boolean isCompliant() {
        return compliant;
    }

    public void setCompliant(boolean compliant) {
        this.compliant = compliant;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
