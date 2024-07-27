package com.ecommerce.app.controller;


import com.ecommerce.app.dto.OrderRequest;
import com.ecommerce.app.dto.OrderResponse;
import com.ecommerce.app.payload.AppResponse;
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

    @PutMapping("/{id}")
    public AppResponse<OrderResponse> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderRequest orderRequest) {
        return AppResponse.ok(orderService.updateOrder(id, orderRequest));
    }

    @DeleteMapping("/{id}")
    public AppResponse<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return AppResponse.noContent();
    }
}
