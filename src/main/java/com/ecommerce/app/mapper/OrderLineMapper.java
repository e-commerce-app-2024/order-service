package com.ecommerce.app.mapper;

import com.ecommerce.app.dto.OrderLine;
import com.ecommerce.app.model.OrderLineEntity;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderLineMapper {

    OrderLine toOrderLine(OrderLineEntity order);

    List<OrderLine> toOrderLine(List<OrderLineEntity> orders);

    OrderLineEntity toOrderLineEntity(OrderLine order);

    List<OrderLineEntity> toOrderLineEntity(List<OrderLine> orders);
}
