package com.ecommerce.app.integration.product.adapter;

import com.ecommerce.app.dto.ProductPurchaseRequest;
import com.ecommerce.app.exception.CustomerIntegrationException;
import com.ecommerce.app.integration.product.client.ProductClient;
import com.ecommerce.app.integration.product.model.CreatePurchaseRequest;
import com.ecommerce.app.integration.product.model.ProductInfoResponse;
import com.ecommerce.app.integration.product.model.PurchaseResponse;
import com.ecommerce.app.payload.AppResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductAdapter {

    private static final Logger log = LoggerFactory.getLogger(ProductAdapter.class);
    private final ProductClient productClient;

    public PurchaseResponse purchaseProduct(List<ProductPurchaseRequest> purchaseList, String userName) {
        var purchaseRequest = CreatePurchaseRequest
                .builder()
                .purchaseList(purchaseList)
                .userName(userName)
                .build();
        AppResponse<PurchaseResponse> appResponse = productClient.purchaseProduct(purchaseRequest);
        if (appResponse.success()) {
            return appResponse.payload();
        } else {
            List<String> errors = appResponse.error().errors();
            throw new CustomerIntegrationException(errors.get(0));
        }
    }

    @Cacheable(value = "productsInfo", key = "#ids")
    public List<ProductInfoResponse> getProductsInfo(List<Long> ids) {
        log.info("getProductsInfo from product service for IDs {}", ids);
        AppResponse<List<ProductInfoResponse>> appResponse = productClient.getProductsInfo(ids);
        if (appResponse.success()) {
            return appResponse.payload();
        } else {
            List<String> errors = appResponse.error().errors();
            throw new CustomerIntegrationException(errors.get(0));
        }
    }

}
