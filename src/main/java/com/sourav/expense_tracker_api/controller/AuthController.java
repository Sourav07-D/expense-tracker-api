package com.sourav.expense_tracker_api.controller;

import com.sourav.expense_tracker_api.dto.AuthRequestDTO;
import com.sourav.expense_tracker_api.dto.AuthResponseDTO;
import com.sourav.expense_tracker_api.dto.ApiResponse;
import com.sourav.expense_tracker_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody AuthRequestDTO request) {

        return ApiResponse.builder()
                .success(true)
                .message("Login successful")
                .data(authService.login(request))
                .build();
    }
}