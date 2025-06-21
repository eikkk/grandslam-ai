package com.plainprog.grandslam_ai.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.object.dto.auth.AccountCreationDTO;
import com.plainprog.grandslam_ai.object.dto.util.OperationOutcome;
import com.plainprog.grandslam_ai.object.dto.util.OperationResultDTO;
import com.plainprog.grandslam_ai.object.dto.util.SimpleOperationResultDTO;
import com.plainprog.grandslam_ai.object.request_models.auth.CreateAccountRequest;
import com.plainprog.grandslam_ai.object.response_models.auth.EmailCheckResponse;
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
     * Checks if an email already exists in the system.
     *
     * @param email the email to check
     * @return response with information about whether the email exists
     */
    @GetMapping("/check-email")
    public ResponseEntity<EmailCheckResponse> checkEmailExists(@RequestParam String email) {
        Account account = accountService.getAccountByEmail(email);
        boolean exists = account != null;

        EmailCheckResponse response = new EmailCheckResponse(exists, "");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    /**
     * Creates an account with the given email. Generated random password.
     * Sends an email with registration confirmation link and password.
     * [Covered with]: AccountCreationTest#testCreateAccountEndpoint()
     * @param request request object containing email
     * @return response entity with status code and message
     */
    @PostMapping("/account")
    public ResponseEntity<OperationResultDTO> createAccount(@RequestBody CreateAccountRequest request) {
        AccountCreationDTO accountResult = accountService.createAccount(request.getEmail());
        if (accountResult == null) {
            OperationResultDTO r = new OperationResultDTO(OperationOutcome.FAILURE, "Failed to create account", null);
            return new ResponseEntity<>(r, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (accountResult.isAlreadyExists()) {
            OperationResultDTO r = new OperationResultDTO(OperationOutcome.ALREADY_EXIST, "", "ALREADY_EXISTS");
            return new ResponseEntity<>(r, HttpStatus.OK);
        }
        if (accountResult.getErrorMessage() != null) {
            OperationResultDTO r = new OperationResultDTO(OperationOutcome.FAILURE, "Failed to create account", accountResult.getErrorMessage());
            return new ResponseEntity<>(r, HttpStatus.BAD_REQUEST);
        }
        SimpleOperationResultDTO res = accountService.sendRegistrationEmail(accountResult.getAccount(), accountResult.getAccountSecurity(), accountResult.getPassword());
        if (!res.isSuccess()) {
            OperationResultDTO result = new OperationResultDTO(OperationOutcome.PARTIAL_SUCCESS, "Account created, but email sending failed", res.getMessage());
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        OperationResultDTO result = new OperationResultDTO(OperationOutcome.SUCCESS, "Account created successfully", null);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    /**
     * Send verification email to authorized user. (For cases when user requests new verification email)
     * [Covered with]: EmailVerificationTest#testEmailVerification()
     * @return response entity with status code and message
     */
    @PostMapping("/account/email-verification")
    public ResponseEntity<OperationResultDTO> requestEmailVerification() {
        String email = SessionDataHolder.getPayload().getAccount().getEmail();

        SimpleOperationResultDTO result = accountService.sendVerificationEmail(email);
        if (!result.isSuccess()) {
            OperationResultDTO r = new OperationResultDTO(OperationOutcome.FAILURE, "Failed to send verification email", result.getMessage());
            return new ResponseEntity<>(r, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new OperationResultDTO(OperationOutcome.SUCCESS, "Verification email sent successfully", null), HttpStatus.OK);
    }
    /**
     * Login endpoint. Authenticates user and initiates session.
     * [Covered with]: LoginTest#testLogin()
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
