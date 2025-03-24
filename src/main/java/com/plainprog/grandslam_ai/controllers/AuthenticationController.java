package com.plainprog.grandslam_ai.controllers;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.test_table.TestTable;
import com.plainprog.grandslam_ai.object.dto.auth.AccountCreationDTO;
import com.plainprog.grandslam_ai.object.dto.util.OperationResultDTO;
import com.plainprog.grandslam_ai.object.request_models.auth.CreateAccountRequest;
import com.plainprog.grandslam_ai.object.request_models.auth.VerifyEmailRequest;
import com.plainprog.grandslam_ai.service.account.AccountService;
import com.plainprog.grandslam_ai.service.account.helpers.PassGenHelp;
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
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    AccountService accountService;
    @PostMapping("/account")
    public ResponseEntity<?> createAccount(@RequestBody CreateAccountRequest request) {
        AccountCreationDTO account = accountService.createAccount(request.getEmail());
        if (account == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create account");
        }
        // FIXME: case when sending email failed is not handled. subject for improvement
        accountService.sendRegistrationEmail(request.getEmail(), account.getPassword());
        return ResponseEntity.ok("Account created successfully");
    }
    @PostMapping("/account/email-verification")
    public ResponseEntity<?> verifyAccountEmail(@RequestBody VerifyEmailRequest request) {
        OperationResultDTO result = accountService.verifyEmail(request);
        if (!result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getMessage());
        }
        return ResponseEntity.ok(result.getMessage());
    }
    @PostMapping("/login")
    public ResponseEntity<?> login( HttpSession session) {
        try {
            GrantedAuthority auth = new SimpleGrantedAuthority("ROLE_ADMIN");
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(auth);
            Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "password", authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
            TestTable testTable = new TestTable();
            testTable.setId(10);
            testTable.setStr("Hello World");
            session.setAttribute("TEST_TABLE", testTable);
//            session.setAttribute("username", "admin");
//            session.setAttribute("password", "password");
//            session.setAttribute("authorities", authorities);
//            session.setAttribute("sessionID", session.getId());


            return ResponseEntity.ok("User successfully logged in with session ID: " + session.getId());

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
