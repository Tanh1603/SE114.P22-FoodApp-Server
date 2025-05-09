package io.foodapp.server.mappers.User;

import io.foodapp.server.dtos.User.AddressRequest;
import io.foodapp.server.dtos.User.AddressResponse;
import io.foodapp.server.models.User.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE, unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AddressMapper {
    Address toEntity(AddressRequest dto);
    AddressResponse toDTO(Address entity);
    List<Address> toEntities(List<AddressRequest> dto);
    List<AddressResponse> toDTO(List<Address> entity);

    void updateEntityFromDTO(AddressRequest dto, @MappingTarget Address entity);
}
