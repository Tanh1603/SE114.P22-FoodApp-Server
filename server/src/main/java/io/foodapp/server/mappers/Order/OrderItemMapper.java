package io.foodapp.server.mappers.Order;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import io.foodapp.server.dtos.Order.OrderItemRequest;
import io.foodapp.server.dtos.Order.OrderItemResponse;
import io.foodapp.server.models.Order.Order;
import io.foodapp.server.models.Order.OrderItem;
import io.foodapp.server.repositories.Menu.MenuItemRepository;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderItemMapper {

    OrderItem toEntity(OrderItemRequest dto,
            @Context Order order,
            @Context MenuItemRepository menuItemRepository);

    @Mapping(target = "menuItemName", source = "menuItem.name")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "isDeleted", source = "deleted")
    OrderItemResponse toDTO(OrderItem orderItemRequest);

    List<OrderItem> toEntities(List<OrderItemRequest> dtos);

    List<OrderItemResponse> toDTOs(List<OrderItem> entities);

    @AfterMapping
    default void setRelatedEntities(OrderItemRequest dto, @MappingTarget OrderItem entity,
            @Context Order order,
            @Context MenuItemRepository menuItemRepository) {

        if (dto.getMenuItemId() == null) {
            throw new RuntimeException("Menu item ID is required");
        }
        entity.setOrder(order);
        entity.setMenuItem(menuItemRepository.findById(dto.getMenuItemId())
                .orElseThrow(() -> new RuntimeException("Menu item not found for ID: " + dto.getMenuItemId())));
        entity.setCurrentPrice(entity.getMenuItem().getPrice());
    }

    @BeanMapping(ignoreByDefault = false)
    void updateEntityFromDto(OrderItemRequest dto, @MappingTarget OrderItem entity,
            @Context Order order,
            @Context MenuItemRepository menuItemRepository);

}
