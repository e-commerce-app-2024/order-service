package com.ecommerce.app.service;

import com.ecommerce.app.dto.*;
import com.ecommerce.app.exception.OrderLineNotFoundException;
import com.ecommerce.app.integration.product.adapter.ProductAdapter;
import com.ecommerce.app.integration.product.model.ProductInfoResponse;
import com.ecommerce.app.mapper.OrderLineMapper;
import com.ecommerce.app.model.OrderEntity;
import com.ecommerce.app.model.OrderLineEntity;
import com.ecommerce.app.payload.PageResponse;
import com.ecommerce.app.repo.OrderLineRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderLineServiceImpl implements OrderLineService {

    private final OrderLineMapper orderLineMapper;
    private final OrderLineRepo orderLineRepo;
    private final ProductAdapter productAdapter;

    @Override
    public void saveOrderLine(OrderLine orderLine, OrderEntity order) {
        OrderLineEntity orderLineEntity = orderLineMapper.toOrderLineEntity(orderLine);
        orderLineEntity.setOrder(order);
        orderLineRepo.save(orderLineEntity);
    }

    @Override
    public void saveOrderLine(List<OrderLine> orderLineList, OrderEntity order) {
        List<OrderLineEntity> entityList = orderLineMapper.toOrderLineEntity(orderLineList);
        entityList.stream().forEach(orderLine -> orderLine.setOrder(order));
        orderLineRepo.saveAll(entityList);
    }

    @Override
    public OrderLine getOrder(Long id) {
        return orderLineRepo.findById(id)
                .map(orderLineMapper::toOrderLine)
                .orElseThrow(() -> new OrderLineNotFoundException(id));
    }

    @Override
    public PageResponse<OrderLine> getOrders(OrderLineFilterRequest request) {
        Sort sort = Sort.by(request.sort() != null ? request.sort() : Sort.Direction.DESC, request.sortBy() != null ? request.sortBy() : "createdAt");
        PageRequest pageRequest = PageRequest.of(request.index().intValue(), request.size().intValue(), sort);
        Page<OrderLineEntity> all = request.orderId() != null ? orderLineRepo.findByOrderId(request.orderId(), pageRequest) : orderLineRepo.findAll(pageRequest);
        return new PageResponse<>
                (orderLineMapper.toOrderLine(all.getContent()),
                        all.isLast(),
                        all.getNumber(),
                        all.getSize(),
                        all.getTotalElements(),
                        all.getTotalPages());
    }

    @Override
    public List<OrderReportResponse> getOrderReport(OrderReportFilterRequest request) {
        List<ProductCount> productsCount;
        if (request.from() != null && request.to() != null) {
            productsCount = orderLineRepo.getProductsCount(request.from(), request.to());
        } else {
            productsCount = orderLineRepo.getProductsCount();
        }
        List<Long> productIds = productsCount.stream().map(product -> product.getProductId()).toList();
        List<ProductInfoResponse> productsInfo = productAdapter.getProductsInfo(productIds);
        List<OrderReportResponse> orderReportResponseList = new ArrayList<>();
        productsCount.stream().forEach(
                product -> {
                    ProductInfoResponse productInfo = productsInfo.stream().filter(item -> item.id() == product.getProductId()).findFirst().get();
                    OrderReportResponse orderReportResponse = OrderReportResponse.builder()
                            .productId(product.getProductId())
                            .quantity(product.getQuantity())
                            .availableQuantity(productInfo.quantity())
                            .productName(productInfo.name())
                            .unitPrice(productInfo.price())
                            .totalAmount(calculateTotalAmount(product.getQuantity(), productInfo.price()))
                            .build();
                    orderReportResponseList.add(orderReportResponse);
                }
        );
        return orderReportResponseList;
    }

    private BigDecimal calculateTotalAmount(Long quantity, BigDecimal price) {
        return new BigDecimal(quantity).multiply(price).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
