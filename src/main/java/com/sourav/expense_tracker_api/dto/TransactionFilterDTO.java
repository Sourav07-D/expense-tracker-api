package com.sourav.expense_tracker_api.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TransactionFilterDTO {

    private Long userId;
    private Long categoryId;

    private LocalDate startDate;
    private LocalDate endDate;

    private Double minAmount;
    private Double maxAmount;
}