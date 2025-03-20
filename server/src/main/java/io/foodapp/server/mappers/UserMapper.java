package io.foodapp.server.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.responses.UserResponse;
import io.foodapp.server.models.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends GenericMapper<User, UserResponse> {

}

