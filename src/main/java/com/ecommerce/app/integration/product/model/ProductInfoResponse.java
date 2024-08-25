package com.ecommerce.app.integration.product.model;

import java.io.Serializable;
import java.math.BigDecimal;


public record ProductInfoResponse(
        Long id,
        String name,
        Long quantity,
        BigDecimal price
) implements Serializable {
}
