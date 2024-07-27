package com.ecommerce.app.service;

import com.ecommerce.app.dto.OrderLine;
import com.ecommerce.app.model.OrderEntity;

import java.util.List;

public interface OrderLineService {

    void saveOrderLine(OrderLine orderLine, OrderEntity order);

    void saveOrderLine(List<OrderLine> orderLineList, OrderEntity order);
}
