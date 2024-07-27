package com.ecommerce.app.dto;

import com.ecommerce.app.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
        Long id,
        @NotNull(message = "customer is required")
        @NotEmpty(message = "customer is required")
        @NotBlank(message = "customer is required")
        String customerId,
        String reference,
        @Positive(message = "Amount is invalid")
        BigDecimal amount,
        @NotNull(message = "Payment Method is required")
        PaymentMethod paymentMethod,
        @NotEmpty(message = "At least, purchase one product")
        List<ProductPurchaseRequest> products
) {
}
