package com.plainprog.grandslam_ai.service.competition;

import com.plainprog.grandslam_ai.entity.competitions.Competition;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompetitionDrawBuilderService {

    @Async
    @Transactional
    public void buildCompetitionDraw(Competition competition) {
        // logic here
    }
}
