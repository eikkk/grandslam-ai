package com.plainprog.grandslam_ai.controllers;

import com.plainprog.grandslam_ai.object.dto.util.OperationResultDTO;
import com.plainprog.grandslam_ai.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Controller
public class WebPagesController {
    @Autowired
    private AccountService accountService;
    /**
     * Verification page for email verification.
     * Verifies email token and checks for its expiration.
     * If successful, shows success html, otherwise shows error html.
     * @param token token to verify email and check for expiration
     * @param model to pass attributes to the thymeleaf template
     * @return verification page
     */
    @GetMapping("/verification")
    public String verification(@RequestParam("token") String token, Model model) {
        String decodedToken = URLDecoder.decode(token, StandardCharsets.UTF_8);
        OperationResultDTO result = accountService.verifyEmailToken(decodedToken);

        String message = result.isSuccess() ? "Your email is confirmed.<br>You can close this page."
                : "Verification failed.<br>" + result.getMessage();
        model.addAttribute("successful", result.isSuccess());
        model.addAttribute("message", message);
        return "verification";
    }
}