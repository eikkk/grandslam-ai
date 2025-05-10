package com.plainprog.grandslam_ai.controllers;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.object.dto.util.OperationOutcome;
import com.plainprog.grandslam_ai.object.dto.util.OperationResultDTO;
import com.plainprog.grandslam_ai.object.request_models.gallery.CreateGalleryGroupRequest;
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
}