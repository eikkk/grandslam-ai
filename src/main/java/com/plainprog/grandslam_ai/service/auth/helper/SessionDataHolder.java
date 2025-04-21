package com.plainprog.grandslam_ai.service.auth.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plainprog.grandslam_ai.object.dto.auth.SessionPayloadDTO;
import com.plainprog.grandslam_ai.object.response_models.auth.SessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SessionDataHolder {
    private static final ThreadLocal<SessionData> sessionDataThreadLocal = new ThreadLocal<>();
    private static ObjectMapper objectMapper;

    @Autowired
    public SessionDataHolder(ObjectMapper objectMapper) {
        SessionDataHolder.objectMapper = objectMapper;
    }
    public static void set(SessionData sessionData) {
        sessionDataThreadLocal.set(sessionData);
    }

    public static SessionData get() {
        return sessionDataThreadLocal.get();
    }

    public static SessionPayloadDTO getPayload() {
        return objectMapper.convertValue(get().getData(), SessionPayloadDTO.class);
    }

    public static void clear() {
        sessionDataThreadLocal.remove();
    }
}