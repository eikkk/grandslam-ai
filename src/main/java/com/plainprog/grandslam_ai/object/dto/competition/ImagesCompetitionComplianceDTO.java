package com.plainprog.grandslam_ai.object.dto.competition;

import java.util.List;

public class ImagesCompetitionComplianceDTO {
    private List<ImageCompetitionComplianceDTO> images;

    public ImagesCompetitionComplianceDTO() {
    }
    public ImagesCompetitionComplianceDTO(List<ImageCompetitionComplianceDTO> images) {
        this.images = images;
    }
    public List<ImageCompetitionComplianceDTO> getImages() {
        return images;
    }
    public void setImages(List<ImageCompetitionComplianceDTO> images) {
        this.images = images;
    }
}
