package com.plainprog.grandslam_ai.service.competition;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.competitions.*;
import com.plainprog.grandslam_ai.entity.competitions.projections.CompetitionSubmissionsCount;
import com.plainprog.grandslam_ai.entity.img_gen.Image;
import com.plainprog.grandslam_ai.entity.img_management.GalleryEntry;
import com.plainprog.grandslam_ai.entity.img_management.GalleryEntryRepository;
import com.plainprog.grandslam_ai.object.dto.competition.ImageCompetitionComplianceDTO;
import com.plainprog.grandslam_ai.object.dto.competition.ImagesCompetitionComplianceDTO;
import com.plainprog.grandslam_ai.object.dto.competition.SubmissionVoteCountDTO;
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
    public final int VOTING_QUEUE_CHUNK_SIZE = 6;
    public final int MAX_ALLOWED_COMP_SUBMISSIONS_PER_ACCOUNT = 2;


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
    public Competition getCompetitionById(Long id) {
        return competitionRepository.findById(id).orElse(null);
    }
    public CompetitionTheme getCompetitionThemeById(Integer id) {
        return competitionThemeRepository.findById(id).orElse(null);
    }
    public List<Competition> getCompetitionsByStatus(Competition.CompetitionStatus status) {
        return competitionRepository.findAllByStatus(status);
    }
    //get by status and theme group id
    public List<Competition> getCompetitionsByStatusAndThemeGroupId(Competition.CompetitionStatus status, Integer themeGroupId) {
        return competitionRepository.findAllByStatusAndThemeGroupId(status, themeGroupId);
    }
    /**
     * Submits an image to a competition.
     * This method handles the submission process, including checking if the competition is open,
     * if the image complies with the competition requirements, and if the user has already submitted the maximum allowed images.
     * or if the image is already submitted.
     *
     * @param request  The submission request containing the competition ID and gallery entry ID
     * @param account  The account of the user submitting the image
     * @return OperationResultDTO indicating success or failure
     */
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

        // check if the image is not hidden
        if (galleryEntry.getHiddenAt() != null) {
            return new OperationResultDTO(OperationOutcome.FAILURE, "Image is hidden from gallery and cannot be submitted to competition", null);
        }

        // Validate that image complies with competition theme
        ImageCompetitionComplianceDTO compliance = isImageCompliesWithCompetitionTheme(galleryEntry, competition);
        if (!compliance.isCompliant()) {
            return new OperationResultDTO(OperationOutcome.FAILURE, compliance.getReason(), null);
        }

        // Get already submitted by this account images
        List<CompetitionSubmission> alreadySubmitted = submissionRepository.findAllByAccountIdAndCompetitionId(account.getId(), competition.getId());
        if (alreadySubmitted.size() >= MAX_ALLOWED_COMP_SUBMISSIONS_PER_ACCOUNT) {
            return new OperationResultDTO(OperationOutcome.FAILURE, "You have already submitted the maximum number of images to this competition", null);
        }
        // Check if given image is already submitted
        for (CompetitionSubmission submission : alreadySubmitted) {
            if (submission.getImage().getId().equals(image.getId())) {
                return new OperationResultDTO(OperationOutcome.FAILURE, "You have already submitted this image to the competition", null);
            }
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
            competitionRepository.save(competition);
        }

        return new OperationResultDTO(OperationOutcome.SUCCESS, "Successfully submitted to competition", null);
    }
    /**
     * Checks if the image complies with the competition requirements.
     * Currently, that means if the image's module matches the competition theme's module.
     *
     * @param galleryEntry The gallery entry containing the image
     * @param competition  The competition to check against
     * @return ImageCompetitionComplianceDTO indicating compliance status and reason
     */
    private ImageCompetitionComplianceDTO isImageCompliesWithCompetitionTheme(GalleryEntry galleryEntry, Competition competition) {
        var requiredModuleId = competition.getTheme().getModule().getId();
        var imageModuleId = galleryEntry.getImage().getImgGenModule().getId();
        boolean complies =  requiredModuleId.equals(imageModuleId);
        String reason = "";
        if (!complies){
            String moduleName = competition.getTheme().getModule().getName();
            reason = "Required image of " + moduleName + " type.";
        }
        return new ImageCompetitionComplianceDTO(galleryEntry, complies, reason);
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
    public OpenCompetitionsResponse getOpenCompetitions(Account account) {
        List<Competition> openCompetitions = competitionRepository.findAllByStatus(Competition.CompetitionStatus.OPEN);
        List<CompetitionQueue> upcomingQueue = competitionQueueRepository.findAllByStatus(CompetitionQueue.CompetitionQueueStatus.NEW);

        List<Long> competitionIds = openCompetitions.stream()
                .map(Competition::getId)
                .toList();

        // Get the submissions that account has made to the open competitions
        List<CompetitionSubmission> mySubmissions = submissionRepository.findAllByAccountIdAndCompetitionIdIn(account.getId(), competitionIds);

        // Gen total number of submissions into each competition
        List<CompetitionSubmissionsCount> submissionCounts = submissionRepository.findCompetitionSubmissionCountsByCompetitionIds(competitionIds);

        List<OpenCompetitionItemModel> openCompetitionItems = new ArrayList<>();
        for (Competition competition : openCompetitions) {
            long submissionCount = submissionCounts.stream()
                    .filter(count -> count.getCompetitionId().equals(competition.getId()))
                    .map(CompetitionSubmissionsCount::getSubmissionCount)
                    .findFirst()
                    .orElse(0L);
            List<CompetitionSubmission> mySubmissionsForCompetition = mySubmissions.stream()
                    .filter(submission -> submission.getCompetition().getId().equals(competition.getId()))
                    .toList();
            OpenCompetitionItemModel item = CompetitionMappers.mapToOpenCompetitionItemModel(competition, (int) submissionCount, mySubmissionsForCompetition);
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

        // Save the vote first
        MatchVote vote = new MatchVote(account, match, votedSubmission);
        matchVoteRepository.save(vote);

        // Check if we should finish the match based on deadline
        boolean isPastDeadline = match.getVoteDeadline().compareTo(Instant.now()) < 0;
        checkMatchVotesAndFinishIfClear(match.getCompetition(), match, !isPastDeadline);

        return new OperationResultDTO(OperationOutcome.SUCCESS, "Vote submitted successfully", null);
    }
    /**
     * Helper method to find the vote count for a specific submission from a list of vote counts
     */
    private int findVoteCount(List<SubmissionVoteCountDTO> voteCounts, Long submissionId) {
        long count = voteCounts.stream()
                .filter(vc -> vc.getSubmissionId().equals(submissionId))
                .map(SubmissionVoteCountDTO::getVoteCount)
                .findFirst()
                .orElse(0L);
        return (int)count;
    }
    
    /**
     * Checks vote counts for a match and finishes it if there's a clear winner
     * 
     * @param match The match to check and potentially finish
     * @param checkVoteTarget Whether to check if vote target has been reached
     * @return true if the match was finished, false otherwise
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean checkMatchVotesAndFinishIfClear(Competition competition, CompetitionMatch match, boolean checkVoteTarget) {
        // Get vote counts for both submissions in a single query
        List<SubmissionVoteCountDTO> voteCounts = matchVoteRepository.countVotesBySubmissionForMatchWithLock(match.getId());
        
        // Get submission IDs for easier reference
        Long sub1Id = match.getSubmission1().getId();
        Long sub2Id = match.getSubmission2().getId();
        
        // Find vote counts for each submission
        int votes1 = findVoteCount(voteCounts, sub1Id);
        int votes2 = findVoteCount(voteCounts, sub2Id);
        
        // Only proceed if there's a vote target requirement, and it's been met, or if we're not checking vote target
        if (!checkVoteTarget || votes1 >= match.getVoteTarget() || votes2 >= match.getVoteTarget()) {
            // Only finish if there's a clear winner (no tie)
            if (votes1 != votes2) {
                CompetitionSubmission winnerSubmission = (votes1 > votes2) ? match.getSubmission1() : match.getSubmission2();
                match.setWinnerSubmission(winnerSubmission);
                match.setFinishedAt(Instant.now());
                match = competitionMatchRepository.save(match);
                competitionDrawBuilderService.processMatchResult(competition, match, votes1, votes2);
                return true;
            }
        }
        // If there's a tie or vote target not reached, we don't finish the match
        return false;
    }

    /**
     * Retrieves the voting queue for the given account.
     * The queue contains matches that are not finished, sorted by match start time.
     * It excludes matches that the account has already voted on and matches that the account is a participant in.
     * @param account The account for which to retrieve the voting queue
     * @return VotingQueueResponse containing a list of active match voting info
     */
    @Transactional(readOnly = true)
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

    /**
     * Retrieves all images from the gallery of the account that meet the competition requirements.
     *
     * @param account      The account whose gallery entries are to be checked
     * @param competitionId  The id of competition to check against
     * @return ImagesCompetitionComplianceDTO containing compliant images
     */
    @Transactional
    public ImagesCompetitionComplianceDTO getImagesCompetitionCompliance(Account account, Long competitionId) {
        Competition competition = competitionRepository.findById(competitionId).orElseThrow(() -> new IllegalArgumentException("Competition not found"));
        if (account == null || competition == null) {
            throw new IllegalArgumentException("Account and competition cannot be null");
        }
        // Get all images from the gallery of the account
        List<GalleryEntry> galleryEntries = galleryEntryRepository.findAllByHiddenAtIsNullAndImageOwnerAccountId(account.getId());
        List<ImageCompetitionComplianceDTO> compliantImages = new ArrayList<>();
        for (GalleryEntry entry : galleryEntries) {
            ImageCompetitionComplianceDTO compliance = isImageCompliesWithCompetitionTheme(entry, competition);
            if (compliance.isCompliant()) {
                compliantImages.add(compliance);
            }
        }
        return new ImagesCompetitionComplianceDTO(compliantImages);
    }
}
