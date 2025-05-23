package com.plainprog.grandslam_ai.object.mappers;

import com.plainprog.grandslam_ai.entity.competitions.Competition;
import com.plainprog.grandslam_ai.entity.competitions.CompetitionQueue;
import com.plainprog.grandslam_ai.entity.competitions.CompetitionSubmission;
import com.plainprog.grandslam_ai.object.response_models.competition.MyCompetitionSubmissionModel;
import com.plainprog.grandslam_ai.object.response_models.competition.OpenCompetitionItemModel;
import com.plainprog.grandslam_ai.object.response_models.competition.UpcomingCompetitionItemModel;

import java.util.List;

public class CompetitionMappers {
    public static OpenCompetitionItemModel mapToOpenCompetitionItemModel(Competition competition, int currentParticipants, List<CompetitionSubmission> mySubmissions) {
        return new OpenCompetitionItemModel(
                competition.getId(),
                competition.getTheme().getName(),
                competition.getParticipantsCount(),
                currentParticipants,
                competition.getTheme().getId(),
                mySubmissions.stream()
                        .map(CompetitionMappers::mapToCompetitionSubmission)
                        .toList()
        );
    }

    public static UpcomingCompetitionItemModel mapToUpcomingCompetitionItemModel(CompetitionQueue queueItem) {
        return new UpcomingCompetitionItemModel(
                queueItem.getTheme().getId(),
                queueItem.getTheme().getName()
        );
    }

    public static MyCompetitionSubmissionModel mapToCompetitionSubmission(CompetitionSubmission submission) {
        return new MyCompetitionSubmissionModel(
                submission.getId(),
                submission.getImage().getId(),
                ImageMapper.mapToDTO(submission.getImage())
        );
    }
}
