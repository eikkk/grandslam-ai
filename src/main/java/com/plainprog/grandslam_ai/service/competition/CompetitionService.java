package com.plainprog.grandslam_ai.service.competition;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.competitions.*;
import com.plainprog.grandslam_ai.entity.img_gen.Image;
import com.plainprog.grandslam_ai.entity.img_management.GalleryEntry;
import com.plainprog.grandslam_ai.entity.img_management.GalleryEntryRepository;
import com.plainprog.grandslam_ai.object.dto.util.OperationOutcome;

import com.plainprog.grandslam_ai.object.dto.util.OperationResultDTO;
import com.plainprog.grandslam_ai.object.request_models.competition.SubmissionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    CompetitionSubmissionRepository submissionRepository;
    @Autowired
    GalleryEntryRepository galleryEntryRepository;
    @Autowired
    CompetitionDrawBuilderService competitionDrawBuilderService;

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
}
