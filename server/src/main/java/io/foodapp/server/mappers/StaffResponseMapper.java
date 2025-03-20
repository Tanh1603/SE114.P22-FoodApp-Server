package io.foodapp.server.mappers;

import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapper;
import io.foodapp.server.dtos.responses.StaffResponse;
import io.foodapp.server.models.Staff;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StaffResponseMapper extends GenericMapper<Staff, StaffResponse> {

}
