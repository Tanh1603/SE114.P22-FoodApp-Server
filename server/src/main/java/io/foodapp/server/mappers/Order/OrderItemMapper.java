package io.foodapp.server.mappers.Order;

import io.foodapp.server.dtos.Order.OrderItemRequest;
import io.foodapp.server.dtos.Order.OrderItemResponse;
import io.foodapp.server.models.Order.OrderItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE, unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface OrderItemMapper {

    OrderItem toEntity(OrderItemRequest dto);
    OrderItemResponse toDTO(OrderItem entity);

    List<OrderItem> toEntities(List<OrderItemRequest> dtos);
    List<OrderItemResponse> toDTOs(List<OrderItem> entities);

}
