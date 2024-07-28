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
        return AppResponse.created(orderService.createOrder(request));
    }

    @PostMapping("/filter")
    public AppResponse<PageResponse<OrderResponse>> getCustomerOrders(@Valid @RequestBody OrderFilterRequest request) {
        return AppResponse.ok(orderService.getCustomerOrders(request));
    }

    @PutMapping("/{id}")
    public AppResponse<OrderResponse> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderRequest orderRequest) {
        return AppResponse.ok(orderService.updateOrder(id, orderRequest));
    }

    @GetMapping("/{id}")
    public AppResponse<OrderResponse> getOrder(@PathVariable Long id) {
        return AppResponse.ok(orderService.getOrder(id));
    }

    @DeleteMapping("/{id}")
    public AppResponse<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return AppResponse.noContent();
    }
}
