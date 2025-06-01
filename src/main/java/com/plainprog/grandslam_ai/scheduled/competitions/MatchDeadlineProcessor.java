package com.plainprog.grandslam_ai.scheduled.competitions;

import com.plainprog.grandslam_ai.entity.competitions.CompetitionMatch;
import com.plainprog.grandslam_ai.entity.competitions.CompetitionMatchRepository;
import com.plainprog.grandslam_ai.service.competition.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class MatchDeadlineProcessor {
    @Autowired
    private CompetitionMatchRepository matchRepository;

    @Autowired
    private CompetitionService competitionService;

    /**
     * Scheduled task that runs every minute to check for matches with expired deadlines
     * and processes their results.
     */
    @Scheduled(fixedRate = 60000) // Run every minute
    public void processDeadlines() {
        System.out.println("Checking for matches with voting deadlines...");
        List<CompetitionMatch> deadlineMatches = matchRepository.findUnprocessedMatchesWithExpiredDeadlines(Instant.now());

        if (!deadlineMatches.isEmpty()) {
            System.out.println("Found " + deadlineMatches.size() + " matches with expired voting deadlines");
            for (CompetitionMatch match : deadlineMatches) {
                competitionService.checkMatchVotesAndFinishIfClear(match.getCompetition(), match, false);
            }
        }
    }
}
