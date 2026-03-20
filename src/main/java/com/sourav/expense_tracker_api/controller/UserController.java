package com.sourav.expense_tracker_api.controller;

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
    public UserResponseDTO createUser(@Valid @RequestBody UserRequestDTO dto) {
        return userService.createUser(dto);
    }
}