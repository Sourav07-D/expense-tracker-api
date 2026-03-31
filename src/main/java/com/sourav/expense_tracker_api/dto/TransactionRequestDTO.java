package com.sourav.expense_tracker_api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TransactionRequestDTO {

    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in future")
    private LocalDate date;
}