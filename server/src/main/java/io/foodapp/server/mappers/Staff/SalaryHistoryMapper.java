package io.foodapp.server.mappers.Staff;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Staff.SalaryHistoryDTO;
import io.foodapp.server.mappers.GenericMapper;
import io.foodapp.server.models.StaffModel.SalaryHistory;

@Mapper(componentModel = "spring",  unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SalaryHistoryMapper extends GenericMapper<SalaryHistory, SalaryHistoryDTO> {
    // Additional mapping methods can be defined here if needed
    
    @Override
    @Mapping(source = "staff.id", target = "staffId")
    SalaryHistoryDTO toDTO(SalaryHistory entity);

}
