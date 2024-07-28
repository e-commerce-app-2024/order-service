package com.ecommerce.app.kafka;

import com.ecommerce.app.dto.OrderConfirmation;

public interface OrderProducerService {

    void sendOrderConfirmation(OrderConfirmation orderConfirmation);
}
