package com.plainprog.grandslam_ai.object.response_models.competition;

import java.util.List;

public class VotingQueueResponse {
    private List<ActiveMatchVotingInfo> votingQueue;

    public VotingQueueResponse(List<ActiveMatchVotingInfo> votingQueue) {
        this.votingQueue = votingQueue;
    }

    public List<ActiveMatchVotingInfo> getVotingQueue() {
        return votingQueue;
    }

    public void setVotingQueue(List<ActiveMatchVotingInfo> votingQueue) {
        this.votingQueue = votingQueue;
    }
}
