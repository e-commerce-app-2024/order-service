package com.ecommerce.app.service;

import com.ecommerce.app.dto.OrderLine;
import com.ecommerce.app.dto.OrderLineFilterRequest;
import com.ecommerce.app.dto.OrderReportFilterRequest;
import com.ecommerce.app.dto.OrderReportResponse;
import com.ecommerce.app.model.OrderEntity;
import com.ecommerce.app.payload.PageResponse;

import java.util.List;

public interface OrderLineService {

    void saveOrderLine(OrderLine orderLine, OrderEntity order);

    void saveOrderLine(List<OrderLine> orderLineList, OrderEntity order);

    OrderLine getOrder(Long id);

    PageResponse<OrderLine> getOrders(OrderLineFilterRequest request);

    List<OrderReportResponse> getOrderReport(OrderReportFilterRequest request);
}
