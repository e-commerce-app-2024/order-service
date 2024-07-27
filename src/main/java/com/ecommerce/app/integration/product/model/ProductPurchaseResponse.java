package com.ecommerce.app.integration.product.model;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductPurchaseResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Long quantity,
        BigDecimal totalPrice
) {
}
