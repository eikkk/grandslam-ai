package com.plainprog.grandslam_ai.spring.security.filters;

import com.plainprog.grandslam_ai.object.response_models.auth.SessionData;
import com.plainprog.grandslam_ai.service.auth.AuthService;
import com.plainprog.grandslam_ai.spring.security.config.AnonymousEndpointMatcher;
import com.plainprog.grandslam_ai.service.auth.helper.SessionDataHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SessionValidationFilter extends OncePerRequestFilter {

    @Autowired
    AuthService authService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        AnonymousEndpointMatcher anonymousEndpointMatcher = new AnonymousEndpointMatcher();
        if (anonymousEndpointMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            // Validate session
            SessionData sessionData = authService.validateSession(request);

            if (sessionData != null) {
                // Store session data and set authentication in thread-local
                SessionDataHolder.set(sessionData);


                List<String> roles = sessionData.getUser().getAuthorities();
                List<GrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());


                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(sessionData.getUser().getUsername(), null, authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            filterChain.doFilter(request, response);
        } finally {
            //Clean up thread-local and security context
            SessionDataHolder.clear();
            SecurityContextHolder.clearContext();
        }
    }
}
