package com.ecommerce.app.kafka;

import com.ecommerce.app.integration.product.model.PurchaseResponse;

public interface ProductProducerService {

    void rollbackPurchaseProduct(PurchaseResponse purchaseResponse, String userName);

}
