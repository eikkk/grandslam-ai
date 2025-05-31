package com.plainprog.grandslam_ai.competition;

import com.plainprog.grandslam_ai.BaseEndpointTest;
import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.competitions.CompetitionTheme;
import com.plainprog.grandslam_ai.entity.img_gen.ImgGenModule;
import com.plainprog.grandslam_ai.entity.img_management.GalleryEntry;
import com.plainprog.grandslam_ai.object.dto.competition.ImageCompetitionComplianceDTO;
import com.plainprog.grandslam_ai.object.dto.competition.ImagesCompetitionComplianceDTO;
import com.plainprog.grandslam_ai.object.dto.util.OperationOutcome;
import com.plainprog.grandslam_ai.object.dto.util.OperationResultDTO;
import com.plainprog.grandslam_ai.object.request_models.competition.SubmissionRequest;
import com.plainprog.grandslam_ai.object.response_models.competition.*;
import com.plainprog.grandslam_ai.object.response_models.generation.ImgGenResponse;
import com.plainprog.grandslam_ai.service.account.helper.TestHelper;
import com.plainprog.grandslam_ai.service.competition.CompetitionService;
import com.plainprog.grandslam_ai.service.gallery.GalleryService;
import com.plainprog.grandslam_ai.service.generation.GenerationModulesService;
import com.plainprog.grandslam_ai.service.generation.TestGenerationHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class UserEmulationCompetitionTests  extends BaseEndpointTest {

    private final TestRestTemplate restTemplate;
    private final TestHelper testHelper;
    private final CompetitionService competitionService;
    private final TestGenerationHelper testGenerationHelper;
    private final GenerationModulesService generationModulesService;
    private final GalleryService galleryService;

    // Constructor injection instead of field injection
    @Autowired
    public UserEmulationCompetitionTests(
            TestRestTemplate restTemplate,
            TestHelper testHelper,
            CompetitionService competitionService,
            TestGenerationHelper testGenerationHelper,
            GenerationModulesService generationModulesService,
            GalleryService galleryService) {
        this.restTemplate = restTemplate;
        this.testHelper = testHelper;
        this.competitionService = competitionService;
        this.testGenerationHelper = testGenerationHelper;
        this.generationModulesService = generationModulesService;
        this.galleryService = galleryService;
    }

    private static final int CYCLES_PER_ACCOUNT = 1; // Set number of emulation cycles per user
    private static final int TOTAL_ACCOUNTS = 8; // Set total number of users to simulate
    
    // Enum for test execution mode
    public enum TestExecutionMode {
        SYNC,   // Execute tasks sequentially
        ASYNC   // Execute tasks concurrently (original behavior)
    }
    
    // Configuration for test execution mode
    // Can be changed to SYNC or ASYNC depending on test needs
    private static final TestExecutionMode EXECUTION_MODE = TestExecutionMode.SYNC;

    //get first 5
    private final List<TestHelper.TestAccount> testAccounts = Arrays.stream(TestHelper.TEST_ACCOUNTS)
            .limit(TOTAL_ACCOUNTS)
            .toList();

    @Test
    public void simulateUsersTournamentsAndVotes() throws InterruptedException, ExecutionException {
        System.out.println("Running test in " + EXECUTION_MODE + " mode");
        
        if (EXECUTION_MODE == TestExecutionMode.ASYNC) {
            runAsyncMode();
        } else {
            runSyncMode();
        }
    }
    
    private void runAsyncMode() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(TOTAL_ACCOUNTS);

        List<Callable<Void>> tasks = testAccounts.stream()
                .map(account -> (Callable<Void>) () -> {
                    simulateUserBehavior(account, CYCLES_PER_ACCOUNT);
                    return null;
                })
                .collect(Collectors.toList());

        List<Future<Void>> futures = executor.invokeAll(tasks);
        for (Future<Void> future : futures) {
            future.get(); // This will throw any exception from the task, including AssertionError
        }
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MINUTES);
    }
    
    private void runSyncMode() throws InterruptedException, ExecutionException {
        // In sync mode, we run each user's tasks sequentially
        for (TestHelper.TestAccount account : testAccounts) {
            System.out.println("Starting sequential execution for user: " + account.email());
            simulateUserBehavior(account, CYCLES_PER_ACCOUNT);
            System.out.println("Completed execution for user: " + account.email());
        }
    }

    private void simulateUserBehavior(TestHelper.TestAccount account, int cycles) {
        // Run cycles
        for (int i = 1; i <= cycles; i++) {
            System.out.printf("User %s starting cycle %d%n", account.email(), i);

            try {
                performCycle(account, i);
                Thread.sleep(1000); // delay between cycles
            } catch (Exception e) {
                System.err.printf("Cycle %d for user %s failed: %s%n", i, account.email(), e.getMessage());
            }
        }
    }

    private void performCycle(TestHelper.TestAccount account, int cycle) throws InterruptedException {
        Account accountEntity = testHelper.getAccount(account.email());
        HttpHeaders headers = testHelper.initiateSession(account.email());
        //fetch open competition
        OpenCompetitionsResponse openCompetitionsResponse = fetchOpenCompetitions(accountEntity, headers);
        assertNotNull(openCompetitionsResponse, "Open competitions response is null");
        //join every available competition
        for (OpenCompetitionItemModel competition : openCompetitionsResponse.getCompetitions()) {
            try {
                boolean joined = attemptSubmitToCompetition(accountEntity, competition, headers);
                if (joined) {
                    System.out.printf("Successfully submitted to competition %s for user %s%n", competition.getId(), account.email());
                } else {
                    System.out.printf("Submission to competition %s for user %s was not allowed due to capacity or submission limit%n", competition.getId(), account.email());
                }
            } catch (Exception e) {
                System.err.printf("Failed to submit to competition %s for user %s: %s%n", competition.getId(), account.email(), e.getMessage());
            }
        }
        //fetch matches available for voting
        VotingQueueResponse votingQueueResponse = competitionService.getVotingQueue(accountEntity);
        assertNotNull(votingQueueResponse, "Voting queue response is null");
        //vote for all matches
        for (ActiveMatchVotingInfo match : votingQueueResponse.getVotingQueue()) {
            try {
                Long matchId = match.getMatchId();
                int randomSubmissionIndex = ThreadLocalRandom.current().nextInt(1, 3); //random 1 or 2
                Long submissionId = randomSubmissionIndex == 1 ? match.getSubmissionId1() : match.getSubmissionId2();
                OperationResultDTO voteResult = competitionService.voteForMatch(accountEntity, matchId, submissionId);
                assertNotNull(voteResult, "Vote result is null");
                assertEquals(OperationOutcome.SUCCESS, voteResult.getOperationOutcome(), "Expected successful vote outcome " + voteResult.getMessage() + "\n" + voteResult.getInternalMessage());
                System.out.printf("Successfully voted for match %d by user %s%n", matchId, account.email());
            } catch (Exception e) {
                System.err.printf("Failed to vote for match %s for user %s: %s%n", match.getMatchId(), account.email(), e.getMessage());
            }
        }

        System.out.printf("Cycle %d completed for user %s%n", cycle, account.email());
    }

    private OpenCompetitionsResponse fetchOpenCompetitions(Account account, HttpHeaders headersWithAuth) {
        headersWithAuth.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Object> entity = new HttpEntity<>(null, headersWithAuth);

        ResponseEntity<OpenCompetitionsResponse> response = restTemplate.exchange(
                baseUrl + "/api/competition/open",
                HttpMethod.GET,
                entity,
                OpenCompetitionsResponse.class
        );

        return response.getBody();
    }

    private boolean attemptSubmitToCompetition(Account account, OpenCompetitionItemModel competition, HttpHeaders headersWithAuth) throws Exception {
        headersWithAuth.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        //get the module required for competition
        Integer themeId = competition.getThemeId();
        CompetitionTheme competitionTheme = competitionService.getCompetitionThemeById(themeId);
        ImgGenModule requiredModule = competitionTheme.getModule();
        if (requiredModule == null){
            requiredModule = testGenerationHelper.getAnyActiveModule();
        }
        //get already submitted images
        List<Long> alreadySubmittedImageIds = competition.getMySubmissions().stream()
                .map(MyCompetitionSubmissionModel::getImageId)
                .toList();
        //entry that we will submit
        GalleryEntry galleryEntryForSubmission;
        //get the image that correspond competition requirements
        ImagesCompetitionComplianceDTO imagesComplianceDTO = competitionService.getImagesCompetitionCompliance(account, competition.getId());
        List<ImageCompetitionComplianceDTO> compatibleImages = imagesComplianceDTO.getImages().stream()
                .filter(image -> !alreadySubmittedImageIds.contains(image.getGalleryEntry().getImage().getId()))
                .toList();
        if (compatibleImages.isEmpty()) {
            galleryEntryForSubmission = generateGalleryEntry(account, requiredModule.getId());
        } else {
            //get first compatible image
            galleryEntryForSubmission = compatibleImages.get(0).getGalleryEntry();
        }
        //check if any more submissions are allowed
        boolean shouldBeDeclinedDueToCapacity = competition.getCurrentParticipants() >= competition.getCapacity();
        boolean isLimitExceeded = competition.getMySubmissions().size() >= competitionService.MAX_ALLOWED_COMP_SUBMISSIONS_PER_ACCOUNT;
        boolean isSubmissionAllowed = !shouldBeDeclinedDueToCapacity && !isLimitExceeded;

        if (!isSubmissionAllowed) {
            attemptForbiddenSubmissionCount(account, competition, headersWithAuth, galleryEntryForSubmission);
            return false;
        } else {
            //prepare submission request
            SubmissionRequest request = new SubmissionRequest(galleryEntryForSubmission.getId(), competition.getId());
            //submit to competition
            HttpEntity<SubmissionRequest> entityWithRequest = new HttpEntity<>(request, headersWithAuth);

            ResponseEntity<OperationResultDTO> response = restTemplate.exchange(
                    baseUrl + "/api/competition/submission",
                    HttpMethod.POST,
                    entityWithRequest,
                    OperationResultDTO.class);

            OperationResultDTO result = response.getBody();
            assertNotNull(result, "Expected non-null result for submission attempt");
            assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected OK status for submission attempt. " + result.getMessage() + "\n" + result.getInternalMessage() );
            assertEquals(OperationOutcome.SUCCESS, result.getOperationOutcome(), "Expected success outcome for submission");
            return true;
        }
    }
    private void attemptForbiddenSubmissionCount(Account account, OpenCompetitionItemModel competition, HttpHeaders headersWithAuth, GalleryEntry galleryEntry) {
        headersWithAuth.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        SubmissionRequest request = new SubmissionRequest(galleryEntry.getId(), competition.getId());
        HttpEntity<SubmissionRequest> entity = new HttpEntity<>(request, headersWithAuth);

        ResponseEntity<OperationResultDTO> response = restTemplate.exchange(
                baseUrl + "/api/competition/submission",
                HttpMethod.POST,
                entity,
                OperationResultDTO.class
        );

        OperationResultDTO result = response.getBody();
        assertNotNull(result, "Expected non-null result for submission attempt");
        assertEquals(OperationOutcome.FAILURE, result.getOperationOutcome(), "Expected failure outcome for submission due to capacity");
    }
    private GalleryEntry generateGalleryEntry(Account account, int moduleId) throws Exception {
        ImgGenModule module = generationModulesService.getModuleById(moduleId);
        ImgGenResponse imgGenResponse = testGenerationHelper.produceTestUserImage(module, account);

        //promote image to gallery
        galleryService.promoteIncubatorImages(List.of(imgGenResponse.getImageId()), account);
        //get gallery entry
        return galleryService.getGalleryEntryByImageId(imgGenResponse.getImageId());
    }

}
