package com.plainprog.grandslam_ai.controllers;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.object.dto.util.OperationOutcome;
import com.plainprog.grandslam_ai.object.dto.util.OperationResultDTO;
import com.plainprog.grandslam_ai.object.request_models.competition.SubmissionRequest;
import com.plainprog.grandslam_ai.object.response_models.competition.OpenCompetitionsResponse;
import com.plainprog.grandslam_ai.service.auth.helper.SessionDataHolder;
import com.plainprog.grandslam_ai.service.competition.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@Controller
@RequestMapping("/api/competition")
public class CompetitionController {

    @Autowired
    private CompetitionService competitionService;

    /**
     * Enter the competition.
     */
    @PostMapping("/submission")
    public ResponseEntity<OperationResultDTO> submitImageToCompetition(@RequestBody SubmissionRequest request) {
        Account account = SessionDataHolder.getPayload().getAccount();
        try {
            OperationResultDTO result = competitionService.submitToCompetition(request, account);
            if (result.getOperationOutcome() == OperationOutcome.SUCCESS) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            OperationResultDTO operationResultDTO = new OperationResultDTO(
                    OperationOutcome.FAILURE,
                    "Error submitting to competition",
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(operationResultDTO);
        }
    }
    /**
     * Quit from the competition
     */
    @PostMapping("/submission/cancel")
    public ResponseEntity<OperationResultDTO> quitCompetition(@RequestBody SubmissionRequest request) {
        Account account = SessionDataHolder.getPayload().getAccount();
        try {
            OperationResultDTO result = competitionService.quitCompetition(request, account);
            if (result.getOperationOutcome() == OperationOutcome.SUCCESS) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            OperationResultDTO operationResultDTO = new OperationResultDTO(
                    OperationOutcome.FAILURE,
                    "Error quitting competition",
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(operationResultDTO);
        }
    }
    /**
     * Get open and upcoming competitions
     */
    @GetMapping("/open")
    public ResponseEntity<OpenCompetitionsResponse> getOpenCompetitions() {
        try {
            OpenCompetitionsResponse result = competitionService.getOpenCompetitions();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.err.println("Error while fetching open competitions: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
