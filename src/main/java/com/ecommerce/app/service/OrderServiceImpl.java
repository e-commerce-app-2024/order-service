package com.ecommerce.app.service;

import com.ecommerce.app.dto.OrderRequest;
import com.ecommerce.app.dto.OrderResponse;
import com.ecommerce.app.integration.customer.adapter.CustomerAdapter;
import com.ecommerce.app.integration.product.adapter.ProductAdapter;
import com.ecommerce.app.mapper.OrderMapper;
import com.ecommerce.app.repo.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final OrderMapper orderMapper;
    private final CustomerAdapter customerAdapter;
    private final ProductAdapter productAdapter;

    @Override
    public OrderResponse createOrder(OrderRequest request) {

        // check the customer
        var customer = customerAdapter.findCustomer(request.customerId());

        var productPurchaseRes = productAdapter.purchaseProduct(request.products());

        // purchase the product

        // persist order

        // persist order line

        // start the payment process

        // send notification to customer [order confirmation]

        // send notification to customer [payment success]

        return OrderResponse.builder().build();
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {
        return null;
    }

    @Override
    public void deleteOrder(Long id) {

    }
}
