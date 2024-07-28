package com.ecommerce.app.dto;


import lombok.Builder;

@Builder
public record OrderLine(
        Long id,
        Long productId,
        Long quantity
) {
}
