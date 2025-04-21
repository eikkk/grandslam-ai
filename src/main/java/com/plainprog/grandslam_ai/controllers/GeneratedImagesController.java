package com.plainprog.grandslam_ai.controllers;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.object.request_models.generation.ImgGenRequest;
import com.plainprog.grandslam_ai.service.auth.helper.SessionDataHolder;
import com.plainprog.grandslam_ai.service.generation.ImageGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gen")
public class GeneratedImagesController {

    @Autowired
    private ImageGenerationService imageGenerationService;
    /**
     * Endpoint for generating images.
     * */
    @PostMapping("/image")
    public ResponseEntity<?> generateImage(@RequestBody ImgGenRequest request) {
        Account account = SessionDataHolder.getPayload().getAccount();
        try {
            var result = imageGenerationService.generateImage(request, account);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error generating image: " + e.getMessage());
        }
    }
}
