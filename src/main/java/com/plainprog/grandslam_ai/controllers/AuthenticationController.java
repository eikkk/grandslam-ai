package com.plainprog.grandslam_ai.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.plainprog.grandslam_ai.object.dto.auth.AccountCreationDTO;
import com.plainprog.grandslam_ai.object.dto.util.OperationResultDTO;
import com.plainprog.grandslam_ai.object.request_models.auth.CreateAccountRequest;
import com.plainprog.grandslam_ai.service.account.AccountService;
import com.plainprog.grandslam_ai.service.auth.AuthService;
import com.plainprog.grandslam_ai.service.auth.helper.SessionDataHolder;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    AccountService accountService;
    @Autowired
    private AuthService authService;

    /**
     * Creates an account with the given email. Generated random password.
     * Sends an email with registration confirmation link and password.
     * @param request request object containing email
     * @return response entity with status code and message
     */
    @PostMapping("/account")
    public ResponseEntity<?> createAccount(@RequestBody CreateAccountRequest request) {
        AccountCreationDTO result = accountService.createAccount(request.getEmail());
        if (result == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        if (result.getErrorMessage() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getErrorMessage());
        }
        OperationResultDTO res = accountService.sendRegistrationEmail(result.getAccount(), result.getAccountSecurity(), result.getPassword());
        if (!res.isSuccess()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res.getMessage());
        }
        return ResponseEntity.ok("Account created successfully");
    }
    /**
     * Send verification email to authorized user. (For cases when user requests new verification email)
     * @return response entity with status code and message
     */
    @PostMapping("/account/email-verification")
    public ResponseEntity<?> requestEmailVerification() {
        String email = SessionDataHolder.getPayload().getEmail();

        OperationResultDTO result = accountService.sendVerificationEmail(email);
        if (!result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getMessage());
        }
        return ResponseEntity.ok("Verification email sent successfully");
    }
    /**
     * Login endpoint. Authenticates user and initiates session.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(HttpSession session, Principal principal) throws JsonProcessingException {
        // if we are here this means that we passed the LoginFilter and the user is authenticated
        String sessionCookie = authService.initiateSession(principal.getName(), SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        // Prepare new response entity with cookie
        HttpHeaders responseHeaders = new HttpHeaders();
        if (sessionCookie != null) {
            responseHeaders.add(HttpHeaders.SET_COOKIE, sessionCookie);
        }

        return new ResponseEntity<>("User successfully logged in with session ID: " + session.getId(), responseHeaders, HttpStatus.OK);
    }
}
