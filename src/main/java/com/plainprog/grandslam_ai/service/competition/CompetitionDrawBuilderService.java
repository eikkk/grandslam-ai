package com.plainprog.grandslam_ai.service.competition;

import com.plainprog.grandslam_ai.entity.competitions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CompetitionDrawBuilderService {

    @Autowired
    private CompetitionMatchRepository competitionMatchRepository;
    @Autowired
    private CompetitionSubmissionRepository competitionSubmissionRepository;

    @Async
    @Transactional
    public void buildCompetitionDraw(Competition competition) {
        //verify there are no matches already
        if (competitionMatchRepository.countByCompetitionId(competition.getId()) > 0) {
            throw new RuntimeException("Matches already exist for this competition");
        }
        int participantsCount = competition.getParticipantsCount();

        // geek shit to get the number of rounds
        // example: 8 in binary is 1000 → 3 trailing zeros → returns 3 (since 2³ = 8)
        int numberOfRounds = Integer.numberOfTrailingZeros(participantsCount);

        // another geek shit. to check if a number is a power of 2
        // A number is a power of 2 if it has exactly one bit set in its binary representation
        if (participantsCount <= 0 || (participantsCount & (participantsCount - 1)) != 0) {
            throw new RuntimeException("Number of participants must be a power of 2");
        }

        //build the competition map (round -> matches)
        Map<Integer, List<CompetitionMatch>> competitionMap = new HashMap<>();

        // Get all submissions for this competition
        List<CompetitionSubmission> submissions = competitionSubmissionRepository.findAllByCompetitionId(competition.getId());
        // Order submissions by image ELO
        submissions.sort((s1, s2) -> Integer.compare(s2.getImage().getElo(), s1.getImage().getElo()));

        //start with a final and move backwards
        for (int round = numberOfRounds; round > 0; round--) {
            int matchesInRound = (int) Math.pow(2, numberOfRounds - round);
            for (int matchIndex = 0; matchIndex < matchesInRound; matchIndex++) {
                // Gen next match in the draw from competitionMap
                CompetitionMatch nextMatch = null;
                boolean isFinal = (round == numberOfRounds);
                if (!isFinal){
                    // Get the next match from the map
                    int nextRound = round + 1;
                    int nextMatchIndex = matchIndex / 2;
                    List<CompetitionMatch> nextRoundMatches = competitionMap.get(nextRound);
                    if (nextRoundMatches != null && nextMatchIndex < nextRoundMatches.size()) {
                        nextMatch = nextRoundMatches.get(nextMatchIndex);
                    } else {
                        // Unexpected case: next match not found
                        throw new RuntimeException("Next match not found for round " + nextRound + " and index " + nextMatchIndex);
                    }
                }
                // Create a match
                CompetitionMatch match = new CompetitionMatch(competition, round, matchIndex, competition.getVoteTarget(), nextMatch);
                // if it's first round, we need to set distribute the competitors
                if (round == 1){
                    // Get the two submissions for this match
                    int submissionIndex1 = matchIndex * 2;
                    int submissionIndex2 =  participantsCount - 1 - submissionIndex1;
                    match.setSubmission1(submissions.get(submissionIndex1));
                    match.setSubmission2(submissions.get(submissionIndex2));
                }

                // Save the match to the database
                match = competitionMatchRepository.save(match);
                // Add the match to the map
                competitionMap.computeIfAbsent(round, k -> new ArrayList<>()).add(match);
            }
        }
    }
}
