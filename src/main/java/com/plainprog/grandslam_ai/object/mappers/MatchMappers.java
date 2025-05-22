package com.plainprog.grandslam_ai.object.mappers;

import com.plainprog.grandslam_ai.entity.competitions.Competition;
import com.plainprog.grandslam_ai.entity.competitions.CompetitionMatch;
import com.plainprog.grandslam_ai.entity.img_gen.Image;
import com.plainprog.grandslam_ai.object.response_models.competition.ActiveMatchVotingInfo;
import com.plainprog.grandslam_ai.object.response_models.competition.OpenCompetitionItemModel;

public class MatchMappers {
    public static ActiveMatchVotingInfo mapToMatchVotingInfo(CompetitionMatch match, Image image1, Image image2) {
        return new ActiveMatchVotingInfo(
                match.getId(),
                match.getSubmission1().getId(),
                match.getSubmission2().getId(),
                ImageMapper.mapToDTO(image1),
                ImageMapper.mapToDTO(image2)
        );
    }
}
