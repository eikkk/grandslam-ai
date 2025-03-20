package com.plainprog.grandslam_ai.controllers;

import com.plainprog.grandslam_ai.entity.test_table.TestTable;
import jakarta.servlet.http.HttpSession;
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
    @PostMapping("/account")
    public ResponseEntity<?> createAccount() {
        return ResponseEntity.ok("Account created successfully");
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
