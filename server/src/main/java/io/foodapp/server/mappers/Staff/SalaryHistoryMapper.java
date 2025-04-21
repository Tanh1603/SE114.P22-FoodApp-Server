package io.foodapp.server.mappers.Staff;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Staff.SalaryHistoryRequest;
import io.foodapp.server.dtos.Staff.SalaryHistoryResponse;
import io.foodapp.server.models.StaffModel.SalaryHistory;

@Mapper(componentModel = "spring",  unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SalaryHistoryMapper {
    // Additional mapping methods can be defined here if needed
    SalaryHistory toEntity(SalaryHistoryRequest dto);

    @Mapping(target = "staffId", source = "staff.id")
    SalaryHistoryResponse toDTO(SalaryHistory entity);

    List<SalaryHistory> toEntities(List<SalaryHistoryRequest> dtos);
    List<SalaryHistoryResponse> toDTOs(List<SalaryHistory> entities);

}
