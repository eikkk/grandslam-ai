package com.plainprog.grandslam_ai.service.competition;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.competitions.*;
import com.plainprog.grandslam_ai.entity.competitions.projections.CompetitionSubmissionsCount;
import com.plainprog.grandslam_ai.entity.img_gen.Image;
import com.plainprog.grandslam_ai.entity.img_management.GalleryEntry;
import com.plainprog.grandslam_ai.entity.img_management.GalleryEntryRepository;
import com.plainprog.grandslam_ai.object.dto.util.OperationOutcome;

import com.plainprog.grandslam_ai.object.dto.util.OperationResultDTO;
import com.plainprog.grandslam_ai.object.mappers.CompetitionMappers;
import com.plainprog.grandslam_ai.object.mappers.MatchMappers;
import com.plainprog.grandslam_ai.object.request_models.competition.SubmissionRequest;
import com.plainprog.grandslam_ai.object.response_models.competition.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompetitionService {
    private final int VOTING_QUEUE_CHUNK_SIZE = 6;


    @Autowired
    CompetitionRepository competitionRepository;
    @Autowired
    CompetitionThemeGroupRepository competitionThemeGroupRepository;
    @Autowired
    CompetitionThemeRepository competitionThemeRepository;
    @Autowired
    CompetitionQueueRepository competitionQueueRepository;
    @Autowired
    CompetitionSubmissionRepository submissionRepository;
    @Autowired
    GalleryEntryRepository galleryEntryRepository;
    @Autowired
    CompetitionDrawBuilderService competitionDrawBuilderService;
    @Autowired
    CompetitionMatchRepository competitionMatchRepository;
    @Autowired
    MatchVoteRepository matchVoteRepository;

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
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public OperationResultDTO submitToCompetition(SubmissionRequest request, Account account) {
        // Use pessimistic locking to prevent concurrent modifications
        Competition competition = competitionRepository.findByIdWithLock(request.getCompetitionId());

        if (competition == null) {
            return new OperationResultDTO(OperationOutcome.FAILURE, "Competition not found", null);
        }
        if (competition.getStatus() != Competition.CompetitionStatus.OPEN) {
            return new OperationResultDTO(OperationOutcome.FAILURE, "Competition is not open for submissions", null);
        }


        // Check if max competitors limit is reached
        int currentCompetitors = submissionRepository.countByCompetitionIdWithLock(competition.getId());
        if (currentCompetitors >= competition.getParticipantsCount()) {
            return new OperationResultDTO(OperationOutcome.FAILURE, "Competition is full", null);
        }

        // Find gallery entry
        GalleryEntry galleryEntry = galleryEntryRepository.findById(request.getGalleryEntryId()).orElseThrow(
                () -> new RuntimeException("Gallery entry not found"));
        Image image = galleryEntry.getImage();
        if (!image.getOwnerAccount().equals(account)){
            return new OperationResultDTO(OperationOutcome.FAILURE,
                    "You are not the owner of this image", null);
        }

        // Create submission
        CompetitionSubmission submission = new CompetitionSubmission(account.getId(), galleryEntry.getImage(), competition);
        submissionRepository.save(submission);


        // Increment the count (we know exactly one submission was added)
        currentCompetitors++;

        // Check if this submission has filled the competition
        if (currentCompetitors >= competition.getParticipantsCount()) {
            // Start the competition
            competition.setStatus(Competition.CompetitionStatus.STARTED);
            competition = competitionRepository.save(competition);

            try {
                // Build the competition draw asynchronously
                competitionDrawBuilderService.buildCompetitionDraw(competition);
            } catch (Exception e) {
                // Handle the exception (e.g., log it)
                System.out.println("Error while building competition draw: " + e.getMessage());
            }
        }

        return new OperationResultDTO(OperationOutcome.SUCCESS, "Successfully submitted to competition", null);
    }
    /**
     * Quits the competition for the given account.
     *
     * @param request  The submission request containing the competition ID and gallery entry ID
     * @param account  The account of the user quitting the competition
     * @return OperationResultDTO indicating success or failure
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public OperationResultDTO quitCompetition(SubmissionRequest request, Account account) {
        // Use pessimistic locking to prevent concurrent modifications
        Competition competition = competitionRepository.findByIdWithLock(request.getCompetitionId());

        if (competition == null) {
            return new OperationResultDTO(OperationOutcome.FAILURE, "Competition not found", null);
        }

        // Check if the competition is in a state that allows quitting
        if (competition.getStatus() != Competition.CompetitionStatus.OPEN) {
            return new OperationResultDTO(OperationOutcome.FAILURE,
                    "Cannot quit competition that already started", null);
        }

        // Find gallery entry
        GalleryEntry galleryEntry = galleryEntryRepository.findById(request.getGalleryEntryId()).orElseThrow(
                () -> new RuntimeException("Gallery entry not found"));
        Image image = galleryEntry.getImage();
        if (!image.getOwnerAccount().equals(account)){
            return new OperationResultDTO(OperationOutcome.FAILURE,
                    "You are not the owner of this image", null);
        }

        // Find the user's submission with lock to prevent concurrent modifications
        CompetitionSubmission submission = submissionRepository.findByCompetitionIdAndAccountIdAndImageIdWithLock(
                competition.getId(), account.getId(), image.getId());

        if (submission == null) {
            return new OperationResultDTO(OperationOutcome.FAILURE,
                    "No submission found for this competition", null);
        }

        // Delete the submission
        submissionRepository.delete(submission);

        return new OperationResultDTO(OperationOutcome.SUCCESS,
                "Successfully quit competition", null);
    }

    /**
     * Retrieves open competitions and upcoming competitions from the queue.
     *
     * @return OpenCompetitionsResponse containing open and upcoming competitions
     */
    public OpenCompetitionsResponse getOpenCompetitions() {
        List<Competition> openCompetitions = competitionRepository.findAllByStatus(Competition.CompetitionStatus.OPEN);
        List<CompetitionQueue> upcomingQueue = competitionQueueRepository.findAllByStatus(CompetitionQueue.CompetitionQueueStatus.NEW);

        List<Long> competitionIds = openCompetitions.stream()
                .map(Competition::getId)
                .toList();

        List<CompetitionSubmissionsCount> submissionCounts = submissionRepository.findCompetitionSubmissionCountsByCompetitionIds(competitionIds);


        List<OpenCompetitionItemModel> openCompetitionItems = new ArrayList<>();
        for (Competition competition : openCompetitions) {
            long submissionCount = submissionCounts.stream()
                    .filter(count -> count.getCompetitionId().equals(competition.getId()))
                    .map(CompetitionSubmissionsCount::getSubmissionCount)
                    .findFirst()
                    .orElse(0L);
            OpenCompetitionItemModel item = CompetitionMappers.mapToOpenCompetitionItemModel(competition, (int) submissionCount);
            openCompetitionItems.add(item);
        }

        List<UpcomingCompetitionItemModel> upcomingCompetitionItems = upcomingQueue.stream()
                .map(CompetitionMappers::mapToUpcomingCompetitionItemModel)
                .toList();

        return new OpenCompetitionsResponse(openCompetitionItems, upcomingCompetitionItems);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public OperationResultDTO voteForMatch(Account account, Long matchId, Long submissionId) {
        CompetitionMatch match = competitionMatchRepository.findByIdWithLock(matchId);
        if (match == null) {
            return new OperationResultDTO(OperationOutcome.FAILURE, "Match not found", null);
        }
        if (match.getWinnerSubmission() != null) {
            return new OperationResultDTO(OperationOutcome.FAILURE, "Match already has a winner", null);
        }
        if (match.getSubmission1().getAccountId().equals(account.getId()) || match.getSubmission2().getAccountId().equals(account.getId())) {
            return new OperationResultDTO(OperationOutcome.FAILURE, "You cannot vote for your own submission", null);
        }
        // Prevent duplicate votes
        if (matchVoteRepository.existsByAccountIdAndMatchId(account.getId(), matchId)) {
            return new OperationResultDTO(OperationOutcome.FAILURE, "You have already voted for this match", null);
        }


        CompetitionSubmission votedSubmission = null;
        if (match.getSubmission1().getId().equals(submissionId)) {
            votedSubmission = match.getSubmission1();
        } else if (match.getSubmission2().getId().equals(submissionId)) {
            votedSubmission = match.getSubmission2();
        } else {
            return new OperationResultDTO(OperationOutcome.FAILURE, "Invalid submission ID", null);
        }

        boolean shouldFinishMatch = false;
        boolean isPastDeadline = match.getVoteDeadline().compareTo(Instant.now()) < 0;
        if (isPastDeadline) {
            shouldFinishMatch = true;
        } else {
            // Check if the match is already finished
            long votesCount = matchVoteRepository.countByMatchIdAndSubmissionIdWithLock(matchId, submissionId);
            if (votesCount + 1 >= match.getVoteTarget()) {
                shouldFinishMatch = true;
            }
        }

        MatchVote vote = new MatchVote(account, match, votedSubmission);
        matchVoteRepository.save(vote);

        if (shouldFinishMatch) {
            // Set the winner submission
            match.setWinnerSubmission(votedSubmission);
            match.setFinishedAt(Instant.now());
            match = competitionMatchRepository.save(match);
            competitionDrawBuilderService.processMatchResult(match);
        }

        return new OperationResultDTO(OperationOutcome.SUCCESS, "Vote submitted successfully", null);
    }

    public VotingQueueResponse getVotingQueue(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        // get first N(VOTING_QUEUE_CHUNK_SIZE) matches sorted by matchStartedAt from oldest to newest where match is not finished (winnerSubmission is null)
        PageRequest pageRequest = PageRequest.of(0, VOTING_QUEUE_CHUNK_SIZE);
        List<CompetitionMatch> matches = competitionMatchRepository.findUnfinishedMatchesExcludingAccount(account.getId(), pageRequest);
        List<ActiveMatchVotingInfo> votingItems = matches.stream()
                .map(match -> {
                    Image image1 = match.getSubmission1().getImage();
                    Image image2 = match.getSubmission2().getImage();
                    CompetitionSubmission submission2 = match.getSubmission2();
                    return MatchMappers.mapToMatchVotingInfo(match, image1, image2);
                })
                .toList();
        return new VotingQueueResponse(votingItems);
    }
}
