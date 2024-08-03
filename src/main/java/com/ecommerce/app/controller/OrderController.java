package com.ecommerce.app.controller;


import com.ecommerce.app.dto.OrderFilterRequest;
import com.ecommerce.app.dto.OrderRequest;
import com.ecommerce.app.dto.OrderResponse;
import com.ecommerce.app.payload.AppResponse;
import com.ecommerce.app.payload.PageResponse;
import com.ecommerce.app.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public AppResponse<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        OrderResponse order = orderService.createOrder(request);
        return AppResponse.created(order, order.requestId());
    }

    @PostMapping("/filter")
    public AppResponse<PageResponse<OrderResponse>> getCustomerOrders(@Valid @RequestBody OrderFilterRequest request) {
        return AppResponse.ok(orderService.getCustomerOrders(request));
    }

    @GetMapping("/{id}")
    public AppResponse<OrderResponse> getOrder(@PathVariable Long id) {
        return AppResponse.ok(orderService.getOrder(id));
    }

}
