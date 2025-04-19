package io.foodapp.server.mappers.Order;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Order.FoodTableRequest;
import io.foodapp.server.dtos.Order.FoodTableResponse;
import io.foodapp.server.models.Order.FoodTable;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FoodTableMapper {

    FoodTable toEntity(FoodTableRequest coffeeTable);
    FoodTableResponse toDTO(FoodTable coffeeTable);

    List<FoodTable> toEntities(List<FoodTableRequest> coffeeTables);
    List<FoodTableResponse> toDTOs(List<FoodTable> coffeeTables);
    
    @BeanMapping(ignoreByDefault = false)
    void updateEntityFromDto(FoodTableRequest coffeeTableRequest, @MappingTarget FoodTable coffeeTable);
}
