package com.plainprog.grandslam_ai.object.dto.competition;

/**
 * DTO to hold vote count data for a submission in a competition match
 */
public class SubmissionVoteCountDTO {
    private Long submissionId;
    private Long voteCount;

    public SubmissionVoteCountDTO(Long submissionId, Long voteCount) {
        this.submissionId = submissionId;
        this.voteCount = voteCount;
    }

    // Default constructor for JPA
    public SubmissionVoteCountDTO() {
    }

    public Long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }
}
