package io.foodapp.server.mappers.Menu;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Menu.UnitRequest;
import io.foodapp.server.dtos.Menu.UnitResponse;
import io.foodapp.server.models.MenuModel.Unit;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UnitMapper{
    Unit toEntity(UnitRequest unitRequest);
    UnitResponse toDTO(Unit unit);

    List<Unit> toEntities(List<UnitRequest> unitRequests);
    List<UnitResponse> toDTOs(List<Unit> units);
    
    @BeanMapping(ignoreByDefault = false)
    void updateEntityFromDto(UnitRequest unitRequest, @MappingTarget Unit unit);
}
