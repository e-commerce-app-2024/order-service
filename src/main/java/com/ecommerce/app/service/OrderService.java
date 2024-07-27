package com.ecommerce.app.service;

import com.ecommerce.app.dto.OrderRequest;
import com.ecommerce.app.dto.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);

    OrderResponse updateOrder(Long id, OrderRequest orderRequest);

    void deleteOrder(Long id);
}
