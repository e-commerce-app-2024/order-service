package com.ecommerce.app.kafka;

public interface ProductProducerService {

    void rollbackPurchaseProduct(String requestId);
}
