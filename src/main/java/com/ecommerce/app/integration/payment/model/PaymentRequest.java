package com.ecommerce.app.integration.payment.model;

import com.ecommerce.app.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PaymentRequest(
        @NotNull(message = "Customer ID is required")
        String customerId,
        @NotNull(message = "Order ID is required")
        Long orderId,
        @NotNull(message = "Amount is required")
        BigDecimal amount,
        @NotNull(message = "The payment method is required")
        PaymentMethod paymentMethod,
        String orderReference

) {
}
