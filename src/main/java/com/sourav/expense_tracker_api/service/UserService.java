package com.sourav.expense_tracker_api.service;

import com.sourav.expense_tracker_api.dto.UserRequestDTO;
import com.sourav.expense_tracker_api.dto.UserResponseDTO;
import com.sourav.expense_tracker_api.entity.User;
import com.sourav.expense_tracker_api.mapper.UserMapper;
import com.sourav.expense_tracker_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO createUser(UserRequestDTO dto) {

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        return UserMapper.toDTO(savedUser);
    }
}