package com.plainprog.grandslam_ai.controllers;

import com.plainprog.grandslam_ai.bean.RedisSessionExplorer;
import com.plainprog.grandslam_ai.model.db.TestTable;
import com.plainprog.grandslam_ai.repository.TestRepository;
import com.plainprog.grandslam_ai.service.TestService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class TestController {

    @Autowired
    TestService testService;

    @Autowired
    RedisSerializer<Object> serializer;

    @GetMapping("/hello")
    public String sayHello(@RequestParam(value = "myName", defaultValue = "World") String name, Principal principal, HttpSession session) throws Exception {
        TestTable obj = testService.getTestTable();
        System.out.println(principal.getName());
        String sessionKey = "spring:session:sessions:" + session.getId();
        RedisSessionExplorer explorer = new RedisSessionExplorer(serializer);
        explorer.inspectSession(sessionKey);
        return String.format("Hello %s!", obj.getStr());
    }
    @GetMapping("/anonymous-endpoint")
    public String anonymousAccess() {
//        RedisSessionExplorer.inspectSession("spring:session:sessions:223b0ebe-cd64-4746-a9e8-97fd373c543c");
        return "This is an anonymous endpoint!";
    }
}
