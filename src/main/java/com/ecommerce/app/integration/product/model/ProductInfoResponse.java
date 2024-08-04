package com.ecommerce.app.integration.product.model;

import java.math.BigDecimal;


public record ProductInfoResponse(
        Long id,
        String name,
        Long quantity,
        BigDecimal price
) {
}
