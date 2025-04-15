package io.foodapp.server.mappers.Order;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Order.OrderItemResponse;
import io.foodapp.server.dtos.Order.OrderRequest;
import io.foodapp.server.dtos.Order.OrderResponse;
import io.foodapp.server.models.Order.Order;
import io.foodapp.server.models.Order.OrderItem;
import io.foodapp.server.models.enums.OrderStatus;
import io.foodapp.server.models.enums.VoucherType;
import io.foodapp.server.repositories.Menu.MenuItemRepository;
import io.foodapp.server.repositories.Order.FoodTableRepository;
import io.foodapp.server.repositories.Order.OrderItemRepository;
import io.foodapp.server.repositories.Staff.StaffRepository;
import io.foodapp.server.repositories.User.VoucherRepository;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {
        OrderItemMapper.class })
public interface OrderMapper {

    Order toEntity(OrderRequest dto,
            @Context FoodTableRepository foodTableRepository,
            @Context VoucherRepository voucherRepository,
            @Context StaffRepository staffRepository,
            @Context MenuItemRepository menuItemRepository,
            @Context OrderItemMapper orderItemMapper,
            @Context OrderItemRepository orderItemRepository);

    @Mapping(target = "tableNumber", source = "table.tableNumber")
    @Mapping(target = "staffName", source = "staff.fullName")
    @Mapping(target = "isDeleted", source = "deleted")
    OrderResponse toDTO(Order entity);

    List<Order> toEntities(List<OrderRequest> dtos);

    List<OrderResponse> toDTOs(List<Order> entities);

    @BeanMapping(ignoreByDefault = false)
    void updateEntityFromDto(OrderRequest dto, @MappingTarget Order entity,
            @Context FoodTableRepository foodTableRepository,
            @Context VoucherRepository voucherRepository,
            @Context StaffRepository staffRepository,
            @Context MenuItemRepository menuItemRepository,
            @Context OrderItemMapper orderItemMapper,
            @Context OrderItemRepository orderItemRepository);

    @AfterMapping
    default void setRelatedEntities(OrderRequest dto, @MappingTarget Order entity,
            @Context FoodTableRepository foodTableRepository,
            @Context VoucherRepository voucherRepository,
            @Context StaffRepository staffRepository,
            @Context MenuItemRepository menuItemRepository,
            @Context OrderItemMapper orderItemMapper,
            @Context OrderItemRepository orderItemRepository) {

        if (dto.getCustomerId() != null) {
            entity.setStatus(OrderStatus.PENDING);
        } else {
            entity.setStatus(OrderStatus.CONFIRMED);
        }

        if (dto.getFoodTableId() != null) {
            entity.setTable(foodTableRepository
                    .findById(dto.getFoodTableId()).orElseThrow(() -> new RuntimeException("Food table not found")));
        } else {
            entity.setTable(null);
        }

        if (dto.getVoucherId() != null) {
            entity.setVoucher(voucherRepository
                    .findById(dto.getVoucherId()).orElseThrow(() -> new RuntimeException("Voucher not found")));
        } else {
            entity.setVoucher(null);
        }
        if (dto.getStaffId() != null) {
            entity.setStaff(staffRepository
                    .findById(dto.getStaffId()).orElseThrow(() -> new RuntimeException("Staff not found")));
        } else {
            entity.setStaff(null);
        }

        if (dto.getOrderItems() != null) {
            entity.setOrderItems(dto.getOrderItems().stream().map(item -> {
                OrderItem upsert;
                if (item.getId() == null) {
                    upsert = orderItemMapper.toEntity(item, entity, menuItemRepository);
                } else {
                    upsert = orderItemRepository.findById(item.getId())
                            .orElseThrow(() -> new RuntimeException("Order item not found for ID: " + item.getId()));
                    orderItemMapper.updateEntityFromDto(item, upsert, entity, menuItemRepository);
                }
                return upsert;
            }).collect(Collectors.toList()));
        }
    }

    @AfterMapping
    default void handleToResponse(@MappingTarget OrderResponse dto, Order entity) {
        if (dto.getOrderItems() != null) {
            List<OrderItemResponse> filteredItems = dto.getOrderItems().stream()
                    .filter(item -> !item.isDeleted())
                    .toList();
            double disCount = 0;
            double temp = 0;
            double total = filteredItems.stream()
                    .mapToDouble(item -> item.getCurrentPrice() * item.getQuantity())
                    .sum();
            var voucher = entity.getVoucher();
            if (voucher != null) {
                if (voucher.getType() == VoucherType.PERCENTAGE) {
                     temp = total * (voucher.getValue() / 100);
                } else {
                    temp = voucher.getValue();
                }
                disCount = Math.min(temp, voucher.getMaxOrderValue());
            }
            dto.setTotalPrice(total - disCount);
            dto.setVoucherDiscount(disCount);
            dto.setOrderItems(filteredItems);
        }
    }
}
