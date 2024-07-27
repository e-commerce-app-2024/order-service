package com.ecommerce.app.dto;


public record OrderLine(
        Long productId,
        Long quantity
) {
}
