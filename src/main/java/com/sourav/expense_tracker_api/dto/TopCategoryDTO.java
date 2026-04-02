package com.sourav.expense_tracker_api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopCategoryDTO {
    private Long categoryId;
    private Double totalAmount;
}
