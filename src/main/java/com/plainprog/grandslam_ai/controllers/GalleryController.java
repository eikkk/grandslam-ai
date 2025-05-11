package com.plainprog.grandslam_ai.controllers;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.object.dto.util.OperationOutcome;
import com.plainprog.grandslam_ai.object.dto.util.OperationResultDTO;
import com.plainprog.grandslam_ai.object.request_models.gallery.CreateGalleryGroupRequest;
import com.plainprog.grandslam_ai.object.request_models.gallery.GroupsChangeOrderRequest;
import com.plainprog.grandslam_ai.object.request_models.gallery.UpdateGalleryRequest;
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
        Account account = SessionDataHolder.getPayload().getAccount();
        galleryService.updateGroup(id, request, account);
        return ResponseEntity.ok(new OperationResultDTO(OperationOutcome.SUCCESS, "Gallery group updated successfully", ""));
    }

    /**
     * Endpoint for changing the order of a gallery group.
     * [Covered with]: GalleryTests#testChangeGalleryGroupOrder()
     */
    @PostMapping("/groups/{id}/order")
    public ResponseEntity<OperationResultDTO> changeGroupOrder(
            @PathVariable Integer id,
            @RequestBody GroupsChangeOrderRequest request) {
        Account account = SessionDataHolder.getPayload().getAccount();
        galleryService.changeGroupOrder(id, request, account);
        return ResponseEntity.ok(new OperationResultDTO(OperationOutcome.SUCCESS, "Gallery group order updated successfully", ""));
    }
}