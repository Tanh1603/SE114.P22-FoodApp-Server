package io.foodapp.server.mappers.Staff;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Staff.StaffDTO;
import io.foodapp.server.mappers.GenericMapper;
import io.foodapp.server.models.StaffModel.Staff;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StaffMapper extends GenericMapper<Staff, StaffDTO> {
    // Additional mapping methods can be defined here if needed

}
