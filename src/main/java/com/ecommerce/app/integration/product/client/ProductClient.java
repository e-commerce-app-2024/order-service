package com.ecommerce.app.integration.product.client;

import com.ecommerce.app.integration.product.model.CreatePurchaseRequest;
import com.ecommerce.app.integration.product.model.ProductInfoResponse;
import com.ecommerce.app.integration.product.model.PurchaseResponse;
import com.ecommerce.app.payload.AppResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "product-service",
        url = "${spring.config.product-url}"
)
public interface ProductClient {

    @PostMapping("/purchase")
    AppResponse<PurchaseResponse> purchaseProduct(@Valid @RequestBody CreatePurchaseRequest request);

    @PostMapping("/info")
    AppResponse<List<ProductInfoResponse>> getProductsInfo(@RequestBody List<Long> ids);
}
