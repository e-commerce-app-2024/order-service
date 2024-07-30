package com.ecommerce.app.integration.payment.model;


import com.ecommerce.app.enums.PaymentMethod;

import java.math.BigDecimal;

public record PaymentResponse(
        String customerId,
        Long orderId,
        String reference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Boolean success
) {
}
