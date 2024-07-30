package com.ecommerce.app.service;

import com.ecommerce.app.dto.*;
import com.ecommerce.app.exception.OrderNotFoundException;
import com.ecommerce.app.exception.PaymentFailureException;
import com.ecommerce.app.integration.customer.adapter.CustomerAdapter;
import com.ecommerce.app.integration.payment.adapter.PaymentAdapter;
import com.ecommerce.app.integration.payment.model.PaymentRequest;
import com.ecommerce.app.integration.product.adapter.ProductAdapter;
import com.ecommerce.app.integration.product.model.ProductPurchaseResponse;
import com.ecommerce.app.kafka.OrderProducerService;
import com.ecommerce.app.mapper.OrderMapper;
import com.ecommerce.app.model.OrderEntity;
import com.ecommerce.app.payload.PageResponse;
import com.ecommerce.app.repo.OrderRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private final PaymentAdapter paymentAdapter;
    private final ProductAdapter productAdapter;
    private final OrderProducerService orderProducerService;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        // check the customer
        var customer = this.customerAdapter.findCustomer(request.customerId());

        // purchase the product
        var productPurchaseRes = this.productAdapter.purchaseProduct(request.products());

        // persist the order
        OrderEntity orderEntity = saveOrder(request, productPurchaseRes);

        // persist the order line
        saveOrderLine(productPurchaseRes, orderEntity);

        // prepare the total amount
        BigDecimal totalAmount = getTotalAmount(productPurchaseRes);
        // start the payment process
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .paymentMethod(request.paymentMethod())
                .amount(totalAmount)
                .customerId(customer.id())
                .orderId(orderEntity.getId())
                .build();
        boolean paymentStatus = paymentAdapter.doPayment(paymentRequest);
        if (!paymentStatus) {
            throw new PaymentFailureException("payment process failed");
        }

        // send notification to customer [order confirmation]
        OrderConfirmation orderConfirmation = OrderConfirmation
                .builder()
                .orderReference(orderEntity.getReference())
                .customer(customer)
                .PaymentMethod(orderEntity.getPaymentMethod())
                .products(productPurchaseRes)
                .totalAmount(totalAmount)
                .build();
        this.orderProducerService.sendOrderConfirmation(orderConfirmation);

        return orderMapper.toResponse(orderEntity);
    }

    @Override
    public PageResponse<OrderResponse> getCustomerOrders(OrderFilterRequest request) {
        Sort sort = Sort.by(request.sort() != null ? request.sort() : Sort.Direction.DESC, request.sortBy() != null ? request.sortBy() : "createdAt");
        PageRequest pageRequest = PageRequest.of(request.index().intValue(), request.size().intValue(), sort);
        Page<OrderEntity> all = orderRepo.findByCustomerId(request.customerId(), pageRequest);
        return new PageResponse<>
                (orderMapper.toResponse(all.getContent()),
                        all.isLast(),
                        all.getNumber(),
                        all.getSize(),
                        all.getTotalElements(),
                        all.getTotalPages());
    }

    private OrderEntity saveOrder(OrderRequest request, List<ProductPurchaseResponse> productPurchaseResponses) {
        OrderEntity orderEntity = this.orderMapper.toOrder(request);
        orderEntity.setTotalAmount(getTotalAmount(productPurchaseResponses));
        orderEntity.setReference(UUID.randomUUID().toString());
        return this.orderRepo.save(orderEntity);
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

    private BigDecimal getTotalAmount(List<ProductPurchaseResponse> productPurchaseResponses) {
        double sum = productPurchaseResponses.stream().mapToDouble(response -> response.totalPrice().doubleValue()).sum();
        return new BigDecimal(sum);
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {
        return null;
    }

    @Override
    public OrderResponse getOrder(Long id) {
        return orderRepo.findById(id)
                .map(orderMapper::toResponse)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    public void deleteOrder(Long id) {

    }
}
