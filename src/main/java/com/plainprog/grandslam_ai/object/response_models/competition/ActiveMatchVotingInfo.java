package com.plainprog.grandslam_ai.object.response_models.competition;

import com.plainprog.grandslam_ai.object.dto.image.ImageDTO;

public class ActiveMatchVotingInfo {
    private Long matchId;
    private Long submissionId1;
    private Long submissionId2;
    private ImageDTO image1;
    private ImageDTO image2;

    public ActiveMatchVotingInfo() {
    }

    public ActiveMatchVotingInfo(Long matchId, Long submissionId1, Long submissionId2, ImageDTO image1, ImageDTO image2) {
        this.matchId = matchId;
        this.submissionId1 = submissionId1;
        this.submissionId2 = submissionId2;
        this.image1 = image1;
        this.image2 = image2;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getSubmissionId1() {
        return submissionId1;
    }

    public void setSubmissionId1(Long submissionId1) {
        this.submissionId1 = submissionId1;
    }

    public Long getSubmissionId2() {
        return submissionId2;
    }

    public void setSubmissionId2(Long submissionId2) {
        this.submissionId2 = submissionId2;
    }

    public ImageDTO getImage1() {
        return image1;
    }

    public void setImage1(ImageDTO image1) {
        this.image1 = image1;
    }

    public ImageDTO getImage2() {
        return image2;
    }

    public void setImage2(ImageDTO image2) {
        this.image2 = image2;
    }
}
