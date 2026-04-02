package com.sourav.expense_tracker_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class CategorySummaryDTO {
    private Long categoryId;
    private double totalAmount;
}
