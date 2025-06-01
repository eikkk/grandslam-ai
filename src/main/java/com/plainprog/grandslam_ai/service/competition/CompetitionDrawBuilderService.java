package com.plainprog.grandslam_ai.service.competition;

import com.plainprog.grandslam_ai.entity.competitions.*;
import com.plainprog.grandslam_ai.entity.img_gen.Image;
import com.plainprog.grandslam_ai.helper.competition.EloCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class CompetitionDrawBuilderService {

    public static final int VOTE_DEADLINE_HOURS = 24;

    @Autowired
    private CompetitionMatchRepository competitionMatchRepository;
    @Autowired
    private CompetitionSubmissionRepository competitionSubmissionRepository;
    @Autowired
    private MatchRecordService matchRecordService;

    @Async
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CompletableFuture<Void> buildCompetitionDraw(Competition competition) {
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
        if (submissions.size() != participantsCount){
            throw new RuntimeException("Number of participants mismatch with number of submissions");
        }
        // Order submissions by image ELO
        submissions.sort((s1, s2) -> Integer.compare(s2.getImage().getElo(), s1.getImage().getElo()));

        //start with a final and move backwards
        for (int round = numberOfRounds; round > 0; round--) {
            int roundInverse = numberOfRounds - round + 1; // Calculate round_inverse
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
                CompetitionMatch match = new CompetitionMatch(competition, round, roundInverse, matchIndex, competition.getVoteTarget(), nextMatch);
                // if it's first round, we need to set distribute the competitors
                if (round == 1){
                    // Get the two submissions for this match
                    int submissionIndex1 = matchIndex * 2;
                    int submissionIndex2 =  participantsCount - 1 - submissionIndex1;
                    match.setSubmission1(submissions.get(submissionIndex1));
                    match.setSubmission2(submissions.get(submissionIndex2));
                    match.setStartedAt(Instant.now());

                    Instant voteDeadline = Instant.now().plusSeconds(VOTE_DEADLINE_HOURS * 3600);
                    match.setVoteDeadline(voteDeadline);
                }

                // Save the match to the database
                match = competitionMatchRepository.save(match);
                // Add the match to the map
                competitionMap.computeIfAbsent(round, k -> new ArrayList<>()).add(match);
            }
        }
        // Mark competition as running
        competition.setStatus(Competition.CompetitionStatus.RUNNING);
        return CompletableFuture.completedFuture(null);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void processMatchResult(CompetitionMatch match, int votes1, int votes2) {
        // Get the winner submission
        CompetitionSubmission winnerSubmission = match.getWinnerSubmission();
        if (winnerSubmission == null || winnerSubmission.getId() == null) {
            // No winner submission, cannot process match result
            throw new RuntimeException("Match result cannot be processed: no winner submission");
        }
        // Process the match result for the tournament bracket
        if (match.getNextMatch() != null) {
            // We have to determine if in the next stage winner is submission1 or submission2
            // it will be submission1 if the matchIndex is even, otherwise submission2
            int matchIndex = match.getMatchIndex();
            boolean isEvenMatchIndex = matchIndex % 2 == 0;
            CompetitionMatch nextMatch = competitionMatchRepository.findByIdWithLock(match.getNextMatch().getId());
            if (isEvenMatchIndex) {
                nextMatch.setSubmission1(winnerSubmission);
            } else {
                nextMatch.setSubmission2(winnerSubmission);
            }
            // if both competitors are in the match, set started at
            if (nextMatch.getSubmission1() != null && nextMatch.getSubmission2() != null) {
                nextMatch.setStartedAt(Instant.now());
                // determine vote deadline
                Instant voteDeadline = Instant.now().plusSeconds(VOTE_DEADLINE_HOURS * 3600);
                nextMatch.setVoteDeadline(voteDeadline);
            }
            // Save the next match
            competitionMatchRepository.save(nextMatch);
        }

        // Calculate ELO ratings and save match records
        Image image1 = match.getSubmission1().getImage();
        Image image2 = match.getSubmission2().getImage();

        // Store original ELO values
        int image1StartingElo = image1.getElo() != null ? image1.getElo() : 1000;
        int image2StartingElo = image2.getElo() != null ? image2.getElo() : 1000;

        // Determine the winner
        boolean image1Won = match.getWinnerSubmission().getId().equals(match.getSubmission1().getId());

        // Calculate new ELO ratings
        int[] newRatings = EloCalculator.calculateNewRatings(image1StartingElo, image2StartingElo, image1Won);
        int image1EndingElo = newRatings[0];
        int image2EndingElo = newRatings[1];

        // Update image ELO ratings
        image1.setElo(image1EndingElo);
        image2.setElo(image2EndingElo);

        // Save match records from both images' perspectives
        matchRecordService.createMatchRecordsForBothImages(
                match,
                image1,
                image2,
                votes1,
                votes2,
                image1StartingElo,
                image1EndingElo,
                image2StartingElo,
                image2EndingElo
        );
    }
}
