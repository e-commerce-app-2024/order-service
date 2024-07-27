package com.ecommerce.app.service;

import com.ecommerce.app.dto.OrderLine;
import com.ecommerce.app.dto.OrderRequest;
import com.ecommerce.app.dto.OrderResponse;
import com.ecommerce.app.integration.customer.adapter.CustomerAdapter;
import com.ecommerce.app.integration.product.adapter.ProductAdapter;
import com.ecommerce.app.integration.product.model.ProductPurchaseResponse;
import com.ecommerce.app.mapper.OrderMapper;
import com.ecommerce.app.model.OrderEntity;
import com.ecommerce.app.repo.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final OrderMapper orderMapper;
    private final OrderLineService orderLineService;
    private final CustomerAdapter customerAdapter;
    private final ProductAdapter productAdapter;

    @Override
    public OrderResponse createOrder(OrderRequest request) {

        // check the customer
        var customer = customerAdapter.findCustomer(request.customerId());

        // purchase the product
        var productPurchaseRes = productAdapter.purchaseProduct(request.products());

        // persist the order
        OrderEntity orderEntity = saveOrder(request, productPurchaseRes);

        // persist the order line
        saveOrderLine(productPurchaseRes, orderEntity);

        // TODO:start the payment process

        // TODO:send notification to customer [order confirmation]

        // TODO:send notification to customer [payment success]

        return OrderResponse.builder().build();
    }

    private OrderEntity saveOrder(OrderRequest request, List<ProductPurchaseResponse> productPurchaseResponses) {
        OrderEntity orderEntity = orderMapper.toOrder(request);
        double totalAmount = productPurchaseResponses.stream().mapToDouble(response -> response.totalPrice().doubleValue()).sum();
        orderEntity.setTotalAmount(new BigDecimal(totalAmount));
        orderEntity.setReference(UUID.randomUUID().toString());
        return orderRepo.save(orderEntity);
    }

    private void saveOrderLine(List<ProductPurchaseResponse> productPurchaseRes, OrderEntity orderEntity) {
        List<OrderLine> orderLineList = new ArrayList<>();
        productPurchaseRes.stream().forEach(productPurchase -> {
            var orderLine = OrderLine
                    .builder()
                    .productId(productPurchase.id())
                    .quantity(productPurchase.quantity())
                    .build();
            orderLineList.add(orderLine);
        });
        orderLineService.saveOrderLine(orderLineList, orderEntity);
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {
        return null;
    }

    @Override
    public void deleteOrder(Long id) {

    }
}
