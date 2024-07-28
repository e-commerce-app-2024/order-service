package com.ecommerce.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;

public record OrderFilterRequest(
        @NotNull(message = "customer is required")
        @NotEmpty(message = "customer is required")
        @NotBlank(message = "customer is required")
        String customerId,
        @NotNull(message = "Page index is required")
        Long index,
        @NotNull(message = "Page size is required")
        Long size,
        String sortBy,
        Sort.Direction sort
) {
}
