package io.foodapp.server.mappers.Staff;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Staff.StaffDTO;
import io.foodapp.server.mappers.GenericMapper;
import io.foodapp.server.models.StaffModel.Staff;

@Mapper(componentModel = "spring", uses = SalaryHistoryMapper.class, unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StaffMapper extends GenericMapper<Staff, StaffDTO> {
    // Additional mapping methods can be defined here if needed

    @Override
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "salaryHistories", ignore = true)
    void updateEntityFromDto(StaffDTO dto, @MappingTarget Staff entity);
}
