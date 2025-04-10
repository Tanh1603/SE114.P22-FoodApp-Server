package io.foodapp.server.mappers;

import org.mapstruct.Mapper;

import io.foodapp.server.dtos.AddressDTO;
import io.foodapp.server.models.Address;

@Mapper(componentModel = "spring", unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE, unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AddressMapper extends GenericMapper<Address, AddressDTO> {
    // Additional mapping methods can be defined here if needed
    
}
