package com.ecommerce.app.dto;


import lombok.Builder;

@Builder
public record OrderLine(
        Long orderId,
        Long productId,
        Long quantity
) {
}
