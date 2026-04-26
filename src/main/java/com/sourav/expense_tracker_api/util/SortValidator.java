package com.sourav.expense_tracker_api.util;

import org.springframework.data.domain.Sort;

import java.util.List;

public class SortValidator {

    private static final List<String> ALLOWED_FIELDS =
            List.of("amount", "date", "createdAt");

    public static Sort validate(Sort sort) {

        for (Sort.Order order : sort) {
            if (!ALLOWED_FIELDS.contains(order.getProperty())) {
                throw new RuntimeException("Invalid sort field: " + order.getProperty());
            }
        }

        return sort;
    }
}