package com.plainprog.grandslam_ai.service.competition;

import com.plainprog.grandslam_ai.entity.competitions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompetitionService {
    @Autowired
    CompetitionRepository competitionRepository;
    @Autowired
    CompetitionThemeGroupRepository competitionThemeGroupRepository;
    @Autowired
    CompetitionThemeRepository competitionThemeRepository;
    @Autowired
    CompetitionQueueRepository competitionQueueRepository;

    public List<CompetitionThemeGroup> getAllThemeGroupsByCompetitionStatus(Competition.CompetitionStatus status) {
        return competitionThemeGroupRepository.findAllByCompetitionsStatus(status);
    }
    //get all theme groups that are not disabled
    public List<CompetitionThemeGroup> getAllActiveThemeGroups() {
        return competitionThemeGroupRepository.findAllByDisabledFalse();
    }
    //get all themes of particular group that are not disabled
    public List<CompetitionTheme> getAllActiveThemesByGroup(CompetitionThemeGroup group) {
        return competitionThemeRepository.findAllByThemeGroupIdAndDisabledFalse(group.getId());
    }
    public List<CompetitionQueue> getAllNotProcessedCompetitionsFromQueue(Integer groupId) {
        return competitionQueueRepository.findAllByStatusAndThemeGroupId(CompetitionQueue.CompetitionQueueStatus.NEW, groupId);
    }
    public List<CompetitionQueue> getLastProcessedQueueItemsOfGivenGroup(int size, int groupId) {
        return competitionQueueRepository.findLastProcessedItemsByThemeGroupId(size, groupId);
    }
    public CompetitionQueue saveCompetitionQueue(CompetitionQueue queue) {
        return competitionQueueRepository.save(queue);
    }
    public Competition saveCompetition(Competition competition) {
        return competitionRepository.save(competition);
    }
}
