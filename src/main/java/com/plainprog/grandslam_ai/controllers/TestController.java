package com.plainprog.grandslam_ai.controllers;

import com.plainprog.grandslam_ai.model.db.TestTable;
import com.plainprog.grandslam_ai.service.TestService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    TestService testService;

    @GetMapping("/hello")
    public String sayHello(@RequestParam(value = "myName", defaultValue = "World") String name, Principal principal, HttpSession session) throws Exception {
        TestTable obj = testService.getTestTable();
        System.out.println(obj.getStr());
        return String.format("Hello %s!", principal == null ? "anonymous" : principal.getName());
    }
    @GetMapping("/anonymous-endpoint")
    public String anonymousAccess() {
        return "This is an anonymous endpoint!";
    }
}
