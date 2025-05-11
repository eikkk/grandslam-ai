package com.plainprog.grandslam_ai.controllers;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.object.dto.util.OperationOutcome;
import com.plainprog.grandslam_ai.object.dto.util.OperationResultDTO;
import com.plainprog.grandslam_ai.object.request_models.gallery.*;
import com.plainprog.grandslam_ai.object.request_models.other.BatchOperationOnIntIds;
import com.plainprog.grandslam_ai.object.request_models.other.BatchOperationOnLongIds;
import com.plainprog.grandslam_ai.object.response_models.image_management.gallery.GalleryResponse;
import com.plainprog.grandslam_ai.service.auth.helper.SessionDataHolder;
import com.plainprog.grandslam_ai.service.gallery.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gallery")
public class GalleryController {

    @Autowired
    private GalleryService galleryService;
    /**
     * Endpoint for creating a new gallery group.
     * [Covered with]: GalleryTests#testCreateGalleryGroup()
     */
    @PostMapping("/groups/new")
    public ResponseEntity<OperationResultDTO> createGroup(CreateGalleryGroupRequest request) {
        Account account = SessionDataHolder.getPayload().getAccount();
        galleryService.createGroup(request, account);
        return ResponseEntity.ok(new OperationResultDTO(OperationOutcome.SUCCESS, "Gallery group created successfully", ""));
    }

    /**
     * Endpoint for updating an existing gallery group.
     * [Covered with]: GalleryTests#testUpdateGalleryGroup()
     */
    @PostMapping("/groups/{id}/update")
    public ResponseEntity<OperationResultDTO> updateGroup(
            @PathVariable Integer id,
            @RequestBody UpdateGalleryRequest request) {
        try {
            Account account = SessionDataHolder.getPayload().getAccount();
            galleryService.updateGroup(id, request, account);
            return ResponseEntity.ok(new OperationResultDTO(OperationOutcome.SUCCESS, "Gallery group updated successfully", ""));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new OperationResultDTO(OperationOutcome.FAILURE, e.getMessage(), ""));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new OperationResultDTO(OperationOutcome.FAILURE, "Failed to update gallery group", e.getMessage()));
        }
    }

    /**
     * Endpoint for changing the order of a gallery group.
     * [Covered with]: GalleryTests#testChangeGalleryGroupOrder()
     */
    @PostMapping("/groups/{id}/order")
    public ResponseEntity<OperationResultDTO> changeGroupOrder(
            @PathVariable Integer id,
            @RequestBody GroupsChangeOrderRequest request) {
        try {
            Account account = SessionDataHolder.getPayload().getAccount();
            galleryService.changeGroupOrder(id, request, account);
            return ResponseEntity.ok(new OperationResultDTO(OperationOutcome.SUCCESS, "Gallery group order updated successfully", ""));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new OperationResultDTO(OperationOutcome.FAILURE, e.getMessage(), ""));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new OperationResultDTO(OperationOutcome.FAILURE, "Failed to change gallery group order", e.getMessage()));
        }
    }

    /**
     * Endpoint for batch hiding gallery items.
     * [Not covered with any tests]
     */
    @PostMapping("/items/batch/hide")
    public ResponseEntity<OperationResultDTO> batchHideItems(@RequestBody BatchOperationOnLongIds  request) {
        try {
            Account account = SessionDataHolder.getPayload().getAccount();
            galleryService.batchHideItems(request.getIds(), account);
            return ResponseEntity.ok(new OperationResultDTO(OperationOutcome.SUCCESS, "Gallery items hidden successfully", ""));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new OperationResultDTO(OperationOutcome.FAILURE, e.getMessage(), ""));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new OperationResultDTO(OperationOutcome.FAILURE, "Failed to hide gallery items", e.getMessage()));
        }
    }
    /**
     * Endpoint for batch moving gallery items to a specific group.
     * [Not covered with any tests]
     */
    @PostMapping("/items/batch/move")
    public ResponseEntity<OperationResultDTO> batchMoveItems(@RequestBody BatchMoveGalleryItemsRequest request) {
        try {
            Account account = SessionDataHolder.getPayload().getAccount();
            galleryService.batchMoveItems(request.getIds(), request.getTargetGroupId(), account);
            return ResponseEntity.ok(new OperationResultDTO(OperationOutcome.SUCCESS, "Gallery items moved successfully", ""));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new OperationResultDTO(OperationOutcome.FAILURE, e.getMessage(), ""));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new OperationResultDTO(OperationOutcome.FAILURE, "Failed to move gallery items", e.getMessage()));
        }
    }

    /**
     * Endpoint for deleting a gallery group.
     * Only works for empty groups (with no images in them).
     * [Not covered with any tests]
     */
    @DeleteMapping("/groups/{id}")
    public ResponseEntity<OperationResultDTO> deleteGroup(@PathVariable Integer id) {
        try {
            Account account = SessionDataHolder.getPayload().getAccount();
            galleryService.deleteGroup(id, account);
            return ResponseEntity.ok(new OperationResultDTO(OperationOutcome.SUCCESS, "Gallery group deleted successfully", ""));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new OperationResultDTO(OperationOutcome.FAILURE, e.getMessage(), ""));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new OperationResultDTO(OperationOutcome.FAILURE, "Failed to delete gallery group", e.getMessage()));
        }
    }
    /**
     * Endpoint for batch shortlisting hidden gallery items.
     * [Not covered with any tests]
     */
    @PostMapping("/hidden/batch/shortlist/{value}")
    public ResponseEntity<OperationResultDTO> batchShortlistItems(
            @RequestBody BatchOperationOnLongIds request,
            @PathVariable boolean value) {
        try {
            Account account = SessionDataHolder.getPayload().getAccount();
            galleryService.batchShortlistItems(request.getIds(), value, account);
            return ResponseEntity.ok(new OperationResultDTO(
                    OperationOutcome.SUCCESS,
                    "Gallery items " + (value ? "shortlisted" : "removed from shortlist") + " successfully",
                    ""));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new OperationResultDTO(OperationOutcome.FAILURE, e.getMessage(), ""));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new OperationResultDTO(
                    OperationOutcome.FAILURE,
                    "Failed to update shortlist status",
                    e.getMessage()));
        }
    }
    /**
     * Endpoint for reordering items within a gallery group.
     * [Not covered with any tests]
     */
    @PostMapping("/groups/{id}/items/reorder")
    public ResponseEntity<OperationResultDTO> reorderGroupItems(
            @PathVariable Integer id,
            @RequestBody ReorderGalleryItemsRequest request) {
        try {
            Account account = SessionDataHolder.getPayload().getAccount();
            galleryService.reorderGroupItems(id, request.getItemIds(), account);
            return ResponseEntity.ok(new OperationResultDTO(OperationOutcome.SUCCESS, "Gallery items reordered successfully", ""));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new OperationResultDTO(OperationOutcome.FAILURE, e.getMessage(), ""));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new OperationResultDTO(OperationOutcome.FAILURE, "Failed to reorder gallery items", e.getMessage()));
        }
    }

    /**
     * Endpoint for retrieving the gallery for an account.
     */
    @GetMapping
    public ResponseEntity<?> getGallery() {
        try {
            Account account = SessionDataHolder.getPayload().getAccount();
            GalleryResponse gallery = galleryService.getGallery(account);
            return ResponseEntity.ok(gallery);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new OperationResultDTO(OperationOutcome.FAILURE, "Failed to retrieve gallery", e.getMessage()));
        }
    }
}