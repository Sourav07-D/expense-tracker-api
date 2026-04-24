package com.sourav.expense_tracker_api.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor   // ✅ this already creates constructor
@NoArgsConstructor
@Builder
public class CategorySummaryDTO {

    private Long categoryId;
    private Double totalAmount;
}
