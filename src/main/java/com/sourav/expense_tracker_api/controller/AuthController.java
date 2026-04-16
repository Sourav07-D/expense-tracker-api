package com.sourav.expense_tracker_api.controller;

import com.sourav.expense_tracker_api.dto.AuthRequestDTO;
import com.sourav.expense_tracker_api.dto.AuthResponseDTO;
import com.sourav.expense_tracker_api.dto.ApiResponse;
import com.sourav.expense_tracker_api.dto.RefreshTokenRequestDTO;
import com.sourav.expense_tracker_api.security.JwtService;
import com.sourav.expense_tracker_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody AuthRequestDTO request) {

        return ApiResponse.builder()
                .success(true)
                .message("Login successful")
                .data(authService.login(request))
                .build();
    }
    @PostMapping("/refresh")
    public ApiResponse<?> refreshToken(@RequestBody RefreshTokenRequestDTO request) {

        String email = jwtService.extractEmail(request.getRefreshToken());

        if (!jwtService.isTokenValid(request.getRefreshToken(), email)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String newAccessToken = jwtService.generateToken(email, "USER"); // role fix later

        return ApiResponse.builder()
                .success(true)
                .message("Token refreshed successfully")
                .data(newAccessToken)
                .build();
    }
}