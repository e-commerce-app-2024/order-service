package com.ecommerce.app.service;

import com.ecommerce.app.dto.OrderLine;
import com.ecommerce.app.mapper.OrderLineMapper;
import com.ecommerce.app.model.OrderEntity;
import com.ecommerce.app.model.OrderLineEntity;
import com.ecommerce.app.repo.OrderLineRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderLineServiceImpl implements OrderLineService {

    private final OrderLineMapper orderLineMapper;
    private final OrderLineRepo orderLineRepo;

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
}
