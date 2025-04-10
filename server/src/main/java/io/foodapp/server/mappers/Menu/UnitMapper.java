package io.foodapp.server.mappers.Menu;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Menu.UnitDTO;
import io.foodapp.server.mappers.GenericMapper;
import io.foodapp.server.models.MenuModel.Unit;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UnitMapper extends GenericMapper<Unit, UnitDTO> {
}
