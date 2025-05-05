package com.plainprog.grandslam_ai.controllers;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.object.dto.util.OperationOutcome;
import com.plainprog.grandslam_ai.object.dto.util.OperationResultDTO;
import com.plainprog.grandslam_ai.object.request_models.other.BatchOperationOnLongIds;
import com.plainprog.grandslam_ai.service.auth.helper.SessionDataHolder;
import com.plainprog.grandslam_ai.service.incubator.IncubatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/incubator")
public class IncubatorController {

    @Autowired
    private IncubatorService incubatorService;

    //Endpoint for batch deleting incubator images
    @PostMapping("/batch/delete")
    public ResponseEntity<OperationResultDTO> deleteIncubatorImages(@RequestBody BatchOperationOnLongIds imageIds) {
        Account account = SessionDataHolder.getPayload().getAccount();
        try {
            incubatorService.deleteIncubatorImages(imageIds.getIds(), account);
            return ResponseEntity.ok(new OperationResultDTO(OperationOutcome.SUCCESS, "Incubator images deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new OperationResultDTO(OperationOutcome.FAILURE, "Failed to delete", e.getMessage()));
        }
    }

}
