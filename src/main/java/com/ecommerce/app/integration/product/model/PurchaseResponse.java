package com.ecommerce.app.integration.product.model;

import lombok.Builder;

import java.util.List;

@Builder
public record PurchaseResponse(
        List<ProductPurchaseResponse> products,
        String requestId,
        String token,
        String userName
) {
}
