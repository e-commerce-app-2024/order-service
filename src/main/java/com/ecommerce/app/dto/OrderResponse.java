package com.ecommerce.app.dto;

import com.ecommerce.app.enums.PaymentMethod;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderResponse(
        Long id,
        String customerId,
        String reference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        String requestId
) {
}
