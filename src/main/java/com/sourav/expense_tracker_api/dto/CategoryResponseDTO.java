package com.sourav.expense_tracker_api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponseDTO {

    private Long id;
    private String name;
    private Long userId;
}