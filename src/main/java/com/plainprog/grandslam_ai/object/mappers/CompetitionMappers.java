package com.plainprog.grandslam_ai.object.mappers;

import com.plainprog.grandslam_ai.entity.competitions.Competition;
import com.plainprog.grandslam_ai.entity.competitions.CompetitionQueue;
import com.plainprog.grandslam_ai.object.response_models.competition.OpenCompetitionItemModel;
import com.plainprog.grandslam_ai.object.response_models.competition.UpcomingCompetitionItemModel;

public class CompetitionMappers {
    public static OpenCompetitionItemModel mapToOpenCompetitionItemModel(Competition competition, int currentParticipants) {
        return new OpenCompetitionItemModel(
                competition.getId(),
                competition.getTheme().getName(),
                competition.getParticipantsCount(),
                currentParticipants,
                competition.getTheme().getId()
        );
    }

    public static UpcomingCompetitionItemModel mapToUpcomingCompetitionItemModel(CompetitionQueue queueItem) {
        return new UpcomingCompetitionItemModel(
                queueItem.getTheme().getId(),
                queueItem.getTheme().getName()
        );
    }
}
