package com.sourav.expense_tracker_api.mapper;

import com.sourav.expense_tracker_api.dto.UserResponseDTO;
import com.sourav.expense_tracker_api.entity.User;

public class UserMapper {

    public static UserResponseDTO toDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}