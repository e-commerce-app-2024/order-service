package com.ecommerce.app.service;

import com.ecommerce.app.dto.OrderLine;
import com.ecommerce.app.dto.OrderLineFilterRequest;
import com.ecommerce.app.exception.OrderLineNotFoundException;
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
}
