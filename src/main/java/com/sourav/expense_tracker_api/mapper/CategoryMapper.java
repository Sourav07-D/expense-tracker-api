package com.sourav.expense_tracker_api.mapper;

import com.sourav.expense_tracker_api.dto.CategoryResponseDTO;
import com.sourav.expense_tracker_api.entity.Category;

public class CategoryMapper {

    public static CategoryResponseDTO toDTO(Category c) {
        return CategoryResponseDTO.builder()
                .id(c.getId())
                .name(c.getName())
                .userId(c.getUser().getId())
                .build();
    }
}