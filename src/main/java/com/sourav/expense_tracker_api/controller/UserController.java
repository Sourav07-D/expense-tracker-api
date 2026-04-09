package com.sourav.expense_tracker_api.controller;

import com.sourav.expense_tracker_api.dto.ApiResponse;
import com.sourav.expense_tracker_api.dto.UserRequestDTO;
import com.sourav.expense_tracker_api.dto.UserResponseDTO;
import com.sourav.expense_tracker_api.entity.User;
import com.sourav.expense_tracker_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping
    public ApiResponse<?> createUser(@Valid @RequestBody UserRequestDTO dto) {

        return ApiResponse.builder()
                .success(true)
                .message("User created successfully")
                .data(userService.createUser(dto))
                .build();
    }
    @GetMapping
    public ApiResponse<?> getAllUsers() {

        return ApiResponse.builder()
                .success(true)
                .message("Users fetched successfully")
                .data(userService.getAllUsers())
                .build();
    }
}