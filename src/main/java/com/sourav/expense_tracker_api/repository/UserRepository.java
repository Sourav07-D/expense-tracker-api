package com.sourav.expense_tracker_api.repository;

import com.sourav.expense_tracker_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}