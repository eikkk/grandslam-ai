package com.plainprog.grandslam_ai.controllers;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.object.request_models.generation.ImgGenRequest;
import com.plainprog.grandslam_ai.object.request_models.generation.SeedRegenRequest;
import com.plainprog.grandslam_ai.object.response_models.generation.ImgGenModulesResponse;
import com.plainprog.grandslam_ai.object.response_models.generation.ModulesHealthCheckResponse;
import com.plainprog.grandslam_ai.service.auth.helper.SessionDataHolder;
import com.plainprog.grandslam_ai.service.generation.GenerationModulesService;
import com.plainprog.grandslam_ai.service.generation.ImageGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gen")
public class GeneratedImagesController {

    @Autowired
    private ImageGenerationService imageGenerationService;
    @Autowired
    private GenerationModulesService generationModulesService;

    /**
     * Main endpoint for generating images by users.
     * [Covered with]: ImageGenerationTest#imageGenerationEndpointTest()
     */
    @PostMapping("/image")
    public ResponseEntity<?> generateImage(@RequestBody ImgGenRequest request) {
        Account account = SessionDataHolder.getPayload().getAccount();
        try {
            var result = imageGenerationService.generateImage(request, account, false, 0, 0);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error generating image: " + e.getMessage());
        }
    }
    /**
     * Endpoint for regenerating images based on same request.
     * [Covered with]: ImageGenerationTest#imageRegenerationTest()
     */
    @PostMapping("/image/{imageId}/regen")
    public ResponseEntity<?> regenerateImage(@PathVariable Long imageId) {
        Account account = SessionDataHolder.getPayload().getAccount();
        try {
            var result = imageGenerationService.regenerateImage(imageId, account);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error generating image: " + e.getMessage());
        }
    }
    /**
     * Endpoint for regenerating images based on seed
     * [Covered with]: ImageGenerationTest#seedGenerationTest()
     */
    @PostMapping("/image/{imageId}/seed-regen")
    public ResponseEntity<?> regenerateImageWithSeed(@PathVariable Long imageId, @RequestBody SeedRegenRequest request) {
        Account account = SessionDataHolder.getPayload().getAccount();
        try {
            var result = imageGenerationService.seedRegenerateImage(imageId, account, request.getPrompt());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error generating image: " + e.getMessage());
        }
    }
    /**
     * Fetch all active image generation modules.
     * [Covered with]: ModulesTest#modulesTest()
     */
    @GetMapping(value = "/modules")
    public ResponseEntity<ImgGenModulesResponse> getModules(){
        ImgGenModulesResponse response = generationModulesService.getModules();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    /**
     * Fetches available image generation models from getimg.ai and checks if all modules that we serve there.
     * If not, deactivates them.
     * [Covered with]: ModulesTest#modulesTest()
     */
    @PostMapping(value = "/modules/health_check")
    public ResponseEntity<ModulesHealthCheckResponse> healthCheckModules() {
        //TODO: This endpoint is anonymous, should be subject for some rate limiting
        ModulesHealthCheckResponse response = generationModulesService.healthCheckModules();
        return ResponseEntity.ok(response);
    }
}
