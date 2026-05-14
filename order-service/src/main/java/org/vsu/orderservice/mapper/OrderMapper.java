package org.vsu.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.vsu.orderservice.dto.OrderResponse;
import org.vsu.orderservice.entity.Order;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    OrderResponse mapToResponse(Order entity);

}
