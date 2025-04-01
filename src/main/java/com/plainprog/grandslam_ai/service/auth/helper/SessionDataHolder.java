package com.plainprog.grandslam_ai.service.auth.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plainprog.grandslam_ai.object.dto.auth.SessionPayloadDTO;
import com.plainprog.grandslam_ai.object.response_models.auth.SessionData;

public class SessionDataHolder {
    private static final ThreadLocal<SessionData> sessionDataThreadLocal = new ThreadLocal<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

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