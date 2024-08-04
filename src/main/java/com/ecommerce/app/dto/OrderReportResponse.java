package com.ecommerce.app.dto;


import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderReportResponse(
        Long productId,
        String productName,
        Long quantity,
        Long availableQuantity,
        BigDecimal unitPrice,
        BigDecimal totalAmount
) {
}
