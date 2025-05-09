package io.foodapp.server.mappers.Order;

import io.foodapp.server.dtos.Order.OrderRequest;
import io.foodapp.server.dtos.Order.OrderResponse;
import io.foodapp.server.models.Order.Order;
import io.foodapp.server.models.enums.VoucherType;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {OrderItemMapper.class})
public interface OrderMapper {

    Order toEntity(OrderRequest dto);

    OrderResponse toDTO(Order entity);

    @AfterMapping
    default void handleToResponse(@MappingTarget OrderResponse dto, Order entity) {
        if (dto.getOrderItems() != null) {
            double disCount = 0;
            double total = dto.getOrderItems().stream()
                    .mapToDouble(item -> item.getPrice() * item.getQuantity())
                    .sum();
            var voucher = entity.getVoucher();
            if (voucher != null) {
                if (voucher.getType() == VoucherType.PERCENTAGE) {
                    disCount = Math.min(total * (voucher.getValue() / 100), voucher.getMaxValue());
                } else {
                    disCount = Math.min(voucher.getValue(), voucher.getMaxValue());
                }
            }
            if (entity.getTable() != null) {
                dto.setTableNumber(entity.getTable().getTableNumber());
            }else {
                dto.setTableNumber(null);
            }
            dto.setTotalPrice(total - disCount);
            dto.setVoucherDiscount(disCount);
        }
    }
}
