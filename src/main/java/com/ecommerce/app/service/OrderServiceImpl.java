package com.ecommerce.app.service;

import com.ecommerce.app.dto.*;
import com.ecommerce.app.enums.OrderStatusEnum;
import com.ecommerce.app.enums.PaymentMethod;
import com.ecommerce.app.exception.OrderNotFoundException;
import com.ecommerce.app.exception.PaymentFailureException;
import com.ecommerce.app.integration.customer.adapter.CustomerAdapter;
import com.ecommerce.app.integration.customer.model.CustomerResponse;
import com.ecommerce.app.integration.payment.adapter.PaymentAdapter;
import com.ecommerce.app.integration.payment.model.PaymentRequest;
import com.ecommerce.app.integration.product.adapter.ProductAdapter;
import com.ecommerce.app.integration.product.model.ProductPurchaseResponse;
import com.ecommerce.app.integration.product.model.PurchaseResponse;
import com.ecommerce.app.kafka.OrderProducerService;
import com.ecommerce.app.kafka.ProductProducerService;
import com.ecommerce.app.mapper.OrderMapper;
import com.ecommerce.app.model.OrderEntity;
import com.ecommerce.app.payload.PageResponse;
import com.ecommerce.app.repo.OrderRepo;
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
    private final ProductProducerService productProducerService;

    @Override
    public OrderResponse createOrder(OrderRequest request) {

        // check the customer
        var customer = this.customerAdapter.findCustomer(request.customerId());

        // persist the order
        OrderEntity orderEntity = saveOrder(request, customer.id(), OrderStatusEnum.PENDING);

        // purchase the product
        var purchaseRes = this.productAdapter.purchaseProduct(request.products(), customer.email());

        // update order purchase [PURCHASING]
        orderEntity = updateOrderPurchase(orderEntity, OrderStatusEnum.PURCHASING, purchaseRes);

        // update order status [PENDING_PAYMENT]
        updateOrderStatus(orderEntity, OrderStatusEnum.PENDING_PAYMENT);

        // start the payment process
        boolean paymentStatus = startPayment(request.paymentMethod(), getTotalAmount(purchaseRes.products()), customer.id(), orderEntity);
        if (paymentStatus) {
            // update order payment [PAID]
            updateOrderPayment(orderEntity, request.paymentMethod(), OrderStatusEnum.PAID);
        } else {
            // rollback purchase product
            updateOrderStatus(orderEntity, OrderStatusEnum.PAYMENT_FAILED);
            productProducerService.rollbackPurchaseProduct(purchaseRes, customer.email());
            throw new PaymentFailureException("payment process failed", purchaseRes.requestId());
        }

        // persist the order line
        saveOrderLine(purchaseRes.products(), orderEntity);

        // send notification to customer [order confirmation]
        sendOrderConfirmation(orderEntity, customer, purchaseRes);

        // update order status [APPROVED]
        updateOrderStatus(orderEntity, OrderStatusEnum.APPROVED);
        return orderMapper.toResponse(orderEntity);
    }

    private boolean startPayment(PaymentMethod paymentMethod, BigDecimal totalAmount, String customerId, OrderEntity orderEntity) {
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .paymentMethod(paymentMethod)
                .amount(totalAmount)
                .customerId(customerId)
                .orderId(orderEntity.getId())
                .orderReference(orderEntity.getReference())
                .build();
        return paymentAdapter.doPayment(paymentRequest);
    }

    private void sendOrderConfirmation(OrderEntity orderEntity, CustomerResponse customer, PurchaseResponse purchaseRes) {
        BigDecimal totalAmount = getTotalAmount(purchaseRes.products());
        OrderConfirmation orderConfirmation = OrderConfirmation
                .builder()
                .orderReference(orderEntity.getReference())
                .customer(customer)
                .paymentMethod(orderEntity.getPaymentMethod())
                .products(purchaseRes.products())
                .totalAmount(totalAmount)
                .build();
        this.orderProducerService.sendOrderConfirmation(orderConfirmation);
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

    private OrderEntity saveOrder(OrderRequest request, String customerId, OrderStatusEnum status) {
        OrderEntity orderEntity = this.orderMapper.toOrder(request);
        orderEntity.setReference(UUID.randomUUID().toString());
        orderEntity.setCustomerId(customerId);
        orderEntity.setStatus(status);
        return this.orderRepo.save(orderEntity);
    }

    private OrderEntity updateOrderPurchase(OrderEntity orderEntity, OrderStatusEnum status, PurchaseResponse purchaseResponse) {
        orderEntity.setTotalAmount(getTotalAmount(purchaseResponse.products()));
        orderEntity.setRequestId(purchaseResponse.requestId());
        orderEntity.setStatus(status);
        return this.orderRepo.save(orderEntity);
    }

    private OrderEntity updateOrderStatus(OrderEntity orderEntity, OrderStatusEnum status) {
        orderEntity.setStatus(status);
        return this.orderRepo.save(orderEntity);
    }

    private OrderEntity updateOrderPayment(OrderEntity orderEntity, PaymentMethod paymentMethod, OrderStatusEnum status) {
        orderEntity.setPaymentMethod(paymentMethod);
        orderEntity.setStatus(status);
        return this.orderRepo.save(orderEntity);
    }

    private void saveOrderLine(List<ProductPurchaseResponse> productPurchaseRes, OrderEntity orderEntity) {
        List<OrderLine> orderLineList = new ArrayList<>();
        productPurchaseRes.stream().forEach(product -> {
            var orderLine = OrderLine
                    .builder()
                    .productId(product.id())
                    .quantity(product.quantity())
                    .build();
            orderLineList.add(orderLine);
        });
        orderLineService.saveOrderLine(orderLineList, orderEntity);
    }

    private BigDecimal getTotalAmount(List<ProductPurchaseResponse> productPurchaseResponses) {
        double sum = productPurchaseResponses.stream().mapToDouble(response -> response.totalPrice().doubleValue()).sum();
        return new BigDecimal(sum).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public OrderResponse getOrder(Long id) {
        return orderRepo.findById(id)
                .map(orderMapper::toResponse)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

}
