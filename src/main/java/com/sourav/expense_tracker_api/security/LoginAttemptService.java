package com.sourav.expense_tracker_api.security;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginAttemptService {

    private final int MAX_ATTEMPT = 5;

    private final Map<String, Integer> attempts = new HashMap<>();

    public void loginFailed(String email) {
        attempts.put(email, attempts.getOrDefault(email, 0) + 1);
    }

    public void loginSucceeded(String email) {
        attempts.remove(email);
    }

    public boolean isBlocked(String email) {
        return attempts.getOrDefault(email, 0) >= MAX_ATTEMPT;
    }
}