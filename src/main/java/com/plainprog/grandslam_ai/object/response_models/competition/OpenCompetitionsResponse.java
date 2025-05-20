package com.plainprog.grandslam_ai.object.response_models.competition;

import java.util.List;

public class OpenCompetitionsResponse {
    private List<OpenCompetitionItemModel> competitions;
    private List<UpcomingCompetitionItemModel> upcomingCompetitions;

    public OpenCompetitionsResponse(List<OpenCompetitionItemModel> competitions, List<UpcomingCompetitionItemModel> upcomingCompetitions) {
        this.competitions = competitions;
        this.upcomingCompetitions = upcomingCompetitions;
    }

    public List<OpenCompetitionItemModel> getCompetitions() {
        return competitions;
    }

    public void setCompetitions(List<OpenCompetitionItemModel> competitions) {
        this.competitions = competitions;
    }

    public List<UpcomingCompetitionItemModel> getUpcomingCompetitions() {
        return upcomingCompetitions;
    }

    public void setUpcomingCompetitions(List<UpcomingCompetitionItemModel> upcomingCompetitions) {
        this.upcomingCompetitions = upcomingCompetitions;
    }
}
