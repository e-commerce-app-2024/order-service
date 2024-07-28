package com.ecommerce.app.dto;

import com.ecommerce.app.enums.PaymentMethod;
import com.ecommerce.app.integration.customer.model.CustomerResponse;
import com.ecommerce.app.integration.product.model.ProductPurchaseResponse;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod PaymentMethod,
        CustomerResponse customer,
        List<ProductPurchaseResponse> products
) {
}
