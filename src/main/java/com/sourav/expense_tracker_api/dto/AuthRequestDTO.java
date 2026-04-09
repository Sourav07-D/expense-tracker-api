package com.sourav.expense_tracker_api.dto;

import lombok.Data;

@Data
public class AuthRequestDTO {
    private String email;
    private String password;
}