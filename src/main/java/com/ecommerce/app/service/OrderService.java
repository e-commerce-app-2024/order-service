package com.ecommerce.app.service;

import com.ecommerce.app.dto.OrderFilterRequest;
import com.ecommerce.app.dto.OrderRequest;
import com.ecommerce.app.dto.OrderResponse;
import com.ecommerce.app.payload.PageResponse;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);

    PageResponse<OrderResponse> getCustomerOrders(OrderFilterRequest request);

    OrderResponse getOrder(Long id);

}
