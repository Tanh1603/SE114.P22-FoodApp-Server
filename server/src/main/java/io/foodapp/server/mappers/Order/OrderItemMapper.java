package io.foodapp.server.mappers.Order;

import io.foodapp.server.dtos.Order.OrderItemRequest;
import io.foodapp.server.dtos.Order.OrderItemResponse;
import io.foodapp.server.models.Order.OrderItem;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

import io.foodapp.server.utils.ImageInfo;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE, unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface OrderItemMapper {

    OrderItem toEntity(OrderItemRequest dto);

    @Mapping(target = "foodImages", source = "foodImages", qualifiedByName = "mapImageInfo")
    OrderItemResponse toDTO(OrderItem entity);

    List<OrderItem> toEntities(List<OrderItemRequest> dtos);

    List<OrderItemResponse> toDTOs(List<OrderItem> entities);

    ImageInfo map(ImageInfo info);

    @Named("mapImageInfo")
    default String mapImageInfo(List<ImageInfo> info) {
        if (info == null || info.isEmpty())
            return null;
        return info.get(0).getUrl();
    }
}
