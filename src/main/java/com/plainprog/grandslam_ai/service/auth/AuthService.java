package com.plainprog.grandslam_ai.service.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.account.AccountRepository;
import com.plainprog.grandslam_ai.object.dto.auth.SessionPayloadDTO;
import com.plainprog.grandslam_ai.object.response_models.auth.SessionData;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AuthService {
    @Value("${auth.service.url}")
    private String authServiceUrl;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AccountRepository accountRepository;

    public String initiateSession(String email, Collection<? extends GrantedAuthority> authorities) throws JsonProcessingException {
        Account account = accountRepository.findByEmail(email);

        String url = authServiceUrl + "/api/session/initiate";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Custom-Header", "value");
        headers.set("user", email);
        List<String> authoritiesStrList = new ArrayList<>();
        for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            authoritiesStrList.add(authority.getAuthority());
        }
        headers.set("authorities", String.join(",", authoritiesStrList));
        SessionPayloadDTO payload = new SessionPayloadDTO(account);
        String jsonBody = objectMapper.writeValueAsString(payload);
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
    }
    public SessionData validateSession(HttpServletRequest request) throws IOException {
        String sessionCookie = getSessionCookie(request);

        if (sessionCookie == null) {
            return null;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionCookie);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                authServiceUrl + "/api/session/validate",
                HttpMethod.POST,
                entity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return objectMapper.readValue(response.getBody(), SessionData.class);
        }

        return null;
    }

    private String getSessionCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("SESSION".equals(cookie.getName())) {
                    return cookie.getName() + "=" + cookie.getValue();
                }
            }
        }
        return null;
    }
}
