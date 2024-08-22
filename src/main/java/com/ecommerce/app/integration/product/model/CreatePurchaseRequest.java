package com.ecommerce.app.integration.product.model;

import com.ecommerce.app.dto.ProductPurchaseRequest;
import jakarta.validation.Valid;
import lombok.Builder;

import java.util.List;


@Builder
public record CreatePurchaseRequest(
        @Valid
        List<ProductPurchaseRequest> purchaseList,
        String userName
) {
}
