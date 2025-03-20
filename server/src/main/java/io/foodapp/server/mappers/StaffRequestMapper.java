package io.foodapp.server.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.requests.StaffRequest;
import io.foodapp.server.models.Staff;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StaffRequestMapper extends GenericMapper<Staff, StaffRequest> {

}
