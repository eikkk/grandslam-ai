package com.plainprog.grandslam_ai.spring.security.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.List;

public class AnonymousEndpointMatcher implements RequestMatcher {

    private final List<String> anonymousEndpoints = Arrays.asList("/api/auth/login", "/api/auth/account", "/verification", "/api/gen/modules/health_check");

    @Override
    public boolean matches(HttpServletRequest request) {
        String path = request.getServletPath();
        for (String endpoint : anonymousEndpoints) {
            if (path.equals(endpoint)) {
                return true;
            }
        }
        return false;
    }
}
