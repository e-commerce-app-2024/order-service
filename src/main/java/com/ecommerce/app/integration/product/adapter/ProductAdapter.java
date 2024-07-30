package com.ecommerce.app.integration.product.adapter;

import com.ecommerce.app.dto.ProductPurchaseRequest;
import com.ecommerce.app.exception.CustomerIntegrationException;
import com.ecommerce.app.integration.product.client.ProductClient;
import com.ecommerce.app.integration.product.model.CreatePurchaseRequest;
import com.ecommerce.app.integration.product.model.PurchaseResponse;
import com.ecommerce.app.payload.AppResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductAdapter {

    private final ProductClient productClient;

    public PurchaseResponse purchaseProduct(List<ProductPurchaseRequest> purchaseList) {
        var purchaseRequest = CreatePurchaseRequest
                .builder()
                .purchaseList(purchaseList)
                .build();
        AppResponse<PurchaseResponse> appResponse = productClient.purchaseProduct(purchaseRequest);
        if (appResponse.success()) {
            return appResponse.payload();
        } else {
            List<String> errors = appResponse.error().errors();
            throw new CustomerIntegrationException(errors.get(0));
        }
    }

}
