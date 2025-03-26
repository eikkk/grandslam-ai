package com.plainprog.grandslam_ai.controllers;

import com.plainprog.grandslam_ai.entity.test_table.TestTable;
import com.plainprog.grandslam_ai.object.dto.auth.AccountCreationDTO;
import com.plainprog.grandslam_ai.object.dto.util.OperationResultDTO;
import com.plainprog.grandslam_ai.object.request_models.auth.CreateAccountRequest;
import com.plainprog.grandslam_ai.object.request_models.auth.LoginRequest;
import com.plainprog.grandslam_ai.service.account.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    AccountService accountService;
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
        //FIXME: case when sending email failed is not handled. subject for improvement
        accountService.sendRegistrationEmail(result.getAccount(), result.getAccountSecurity(), result.getPassword());
        return ResponseEntity.ok("Account created successfully");
    }
    /**
     * Send verification email. (For cases when user requests new verification email)
     * @param email email of the account
     * @return response entity with status code and message
     */
    @PostMapping("/account/email-verification")
    public ResponseEntity<?> requestEmailVerification(@RequestHeader String email) {
        //TODO: THIS ENDPOINT SHOULD BE PROTECTED WITH AUTHENTICATION
        //FIXME: case when sending email failed is not handled. subject for improvement
        OperationResultDTO result = accountService.sendVerificationEmail(email);
        if (!result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getMessage());
        }
        return ResponseEntity.ok("Verification email sent successfully");
    }
    /**
     * Temporary.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session) {
//        try {
//            GrantedAuthority auth = new SimpleGrantedAuthority("ROLE_ADMIN");
//            List<GrantedAuthority> authorities = new ArrayList<>();
//            authorities.add(auth);
//            Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "password", authorities);

//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
//            TestTable testTable = new TestTable();
//            testTable.setId(10);
//            testTable.setStr("Hello World");
//            session.setAttribute("TEST_TABLE", testTable);
//            session.setAttribute("username", "admin");
//            session.setAttribute("password", "password");
//            session.setAttribute("authorities", authorities);
//            session.setAttribute("sessionID", session.getId());

        return ResponseEntity.ok("User successfully logged in with session ID: " + session.getId());
    }
}
