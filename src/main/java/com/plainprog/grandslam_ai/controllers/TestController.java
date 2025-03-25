package com.plainprog.grandslam_ai.controllers;

import com.plainprog.grandslam_ai.entity.test_table.TestTable;
import com.plainprog.grandslam_ai.service.TestService;
import com.plainprog.grandslam_ai.service.email.EmailServiceJavaMail;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    TestService testService;
    @Autowired
    EmailServiceJavaMail emailService;

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
    @GetMapping("/test-email")
    public String testEmail() {
        return "NOT IMPLEMENTED";
    }
    @GetMapping("/test-email-template")
    public String testEmailTemplate() {
        Map<String,Object> model = Map.of("password", "QQwwerer234qwe");
        try {
            emailService.sendTemplateEmail("petrodarchyn@gmail.com", "Verification", model, "VerificationAndPasswordEmail.ftl");
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
        return "SENT";
    }
}
