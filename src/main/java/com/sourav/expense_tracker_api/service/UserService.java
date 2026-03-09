package com.sourav.expense_tracker_api.service;

import com.sourav.expense_tracker_api.entity.User;
import com.sourav.expense_tracker_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(User user) {

        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }
}