package com.ecommerce.app.controller;


import com.ecommerce.app.dto.OrderLine;
import com.ecommerce.app.dto.OrderLineFilterRequest;
import com.ecommerce.app.payload.AppResponse;
import com.ecommerce.app.payload.PageResponse;
import com.ecommerce.app.service.OrderLineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order-line")
@RequiredArgsConstructor
public class OrderLineController {

    private final OrderLineService orderLineService;

    @PostMapping
    public AppResponse<PageResponse<OrderLine>> getCustomerOrders(@Valid @RequestBody OrderLineFilterRequest request) {
        return AppResponse.ok(orderLineService.getOrders(request));
    }

    @GetMapping("/{id}")
    public AppResponse<OrderLine> getOrderLine(@PathVariable Long id) {
        return AppResponse.ok(orderLineService.getOrder(id));
    }

}
