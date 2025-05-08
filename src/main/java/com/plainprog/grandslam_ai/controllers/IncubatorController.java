package com.plainprog.grandslam_ai.controllers;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.object.dto.util.OperationOutcome;
import com.plainprog.grandslam_ai.object.dto.util.OperationResultDTO;
import com.plainprog.grandslam_ai.object.request_models.other.BatchOperationOnLongIds;
import com.plainprog.grandslam_ai.object.response_models.image_management.incubator.IncubatorResponseModel;
import com.plainprog.grandslam_ai.service.auth.helper.SessionDataHolder;
import com.plainprog.grandslam_ai.service.gallery.GalleryService;
import com.plainprog.grandslam_ai.service.incubator.IncubatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/incubator")
public class IncubatorController {

    @Autowired
    private IncubatorService incubatorService;
    @Autowired
    private GalleryService galleryService;

    /**
     * Endpoint for deleting incubator images.
     * Also deletes the image itself completely from the system and gcp storage.
     * [Covered with]: IncubatorTests#testIncubatorDeletion()
     */
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
    /**
     * Endpoint for promoting incubator images.
     * Removes incubator entry and creates a gallery entry
     * [Covered with]: IncubatorTests#testIncubatorPromotion()
     */
    @PostMapping("/batch/promote")
    public ResponseEntity<OperationResultDTO> promoteIncubatorImages(@RequestBody BatchOperationOnLongIds imageIds) {
        Account account = SessionDataHolder.getPayload().getAccount();
        try {
            galleryService.promoteIncubatorImages(imageIds.getIds(), account);
            return ResponseEntity.ok(new OperationResultDTO(OperationOutcome.SUCCESS, "Incubator images promoted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new OperationResultDTO(OperationOutcome.FAILURE, "Failed to delete", e.getMessage()));
        }
    }
    /**
     * Endpoint for setting shortlist flag to incubator images
     * [Covered with]: IncubatorTests#testShortlisting()
     */
    @PostMapping("/batch/shortlist/{value}")
    public ResponseEntity<OperationResultDTO> shortlistIncubatorImages(@RequestBody BatchOperationOnLongIds imageIds, @PathVariable("value") Boolean value) {
        try {
            incubatorService.shortlistIncubatorImages(imageIds.getIds(), value);
            return ResponseEntity.ok(new OperationResultDTO(OperationOutcome.SUCCESS, "Incubator images promoted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new OperationResultDTO(OperationOutcome.FAILURE, "Failed to delete", e.getMessage()));
        }
    }

    /**
     * Endpoint for getting all incubator images for authenticated user
     */
    @GetMapping("/all")
    public ResponseEntity<IncubatorResponseModel> getAllIncubatorImages() {
        Account account = SessionDataHolder.getPayload().getAccount();
        try {
            IncubatorResponseModel res = incubatorService.getIncubatorEntries(account);
            res.setOperationResult(new OperationResultDTO(OperationOutcome.SUCCESS, null, null));
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            IncubatorResponseModel res = IncubatorResponseModel.empty();
            res.setOperationResult(new OperationResultDTO(OperationOutcome.FAILURE, "Failed to get incubator images", e.getMessage()));
            return ResponseEntity.status(500).body(res);
        }
    }

}
