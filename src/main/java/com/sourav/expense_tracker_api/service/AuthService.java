package com.sourav.expense_tracker_api.service;

import com.sourav.expense_tracker_api.dto.AuthRequestDTO;
import com.sourav.expense_tracker_api.dto.AuthResponseDTO;
import com.sourav.expense_tracker_api.entity.User;
import com.sourav.expense_tracker_api.exception.ResourceNotFoundException;
import com.sourav.expense_tracker_api.repository.UserRepository;
import com.sourav.expense_tracker_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final  JwtService jwtService;
    public AuthResponseDTO login(AuthRequestDTO request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }


        String accessToken = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        return AuthResponseDTO.builder()
                .message("Login successful")
                .token(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}