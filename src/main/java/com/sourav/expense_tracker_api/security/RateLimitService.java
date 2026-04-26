package com.sourav.expense_tracker_api.security;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class RateLimitService {

    private static final int MAX_REQUESTS = 10; // 🔥 limit
    private static final long TIME_WINDOW = 60; // seconds

    private final Map<String, RequestInfo> requestMap = new HashMap<>();

    public boolean isAllowed(String key) {

        RequestInfo info = requestMap.getOrDefault(key, new RequestInfo());

        long currentTime = Instant.now().getEpochSecond();

        if (currentTime - info.startTime > TIME_WINDOW) {
            // reset window
            info.count = 1;
            info.startTime = currentTime;
        } else {
            info.count++;
        }

        requestMap.put(key, info);

        return info.count <= MAX_REQUESTS;
    }

    static class RequestInfo {
        int count = 0;
        long startTime = Instant.now().getEpochSecond();
    }
}