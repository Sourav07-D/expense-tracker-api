package com.sourav.expense_tracker_api.mapper;

import com.sourav.expense_tracker_api.dto.TransactionResponseDTO;
import com.sourav.expense_tracker_api.entity.Transaction;

public class TransactionMapper {

    public static TransactionResponseDTO toDTO(Transaction t) {
        return TransactionResponseDTO.builder()
                .id(t.getId())
                .amount(t.getAmount())
                .description(t.getDescription())
                .date(t.getDate())
                .userId(t.getUser().getId())
                .categoryId(t.getCategory().getId())
                .build();
    }
}