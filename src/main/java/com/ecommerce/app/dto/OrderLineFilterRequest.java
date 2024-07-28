package com.ecommerce.app.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;

public record OrderLineFilterRequest(
        Long orderId,
        @NotNull(message = "Page index is required")
        Long index,
        @NotNull(message = "Page size is required")
        Long size,
        String sortBy,
        Sort.Direction sort
) {
}
