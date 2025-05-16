package io.foodapp.server.mappers.Staff;

import java.util.List;

import io.foodapp.server.utils.ImageInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import io.foodapp.server.dtos.Staff.StaffRequest;
import io.foodapp.server.dtos.Staff.StaffResponse;
import io.foodapp.server.models.StaffModel.Staff;

@Mapper(componentModel = "spring", uses = SalaryHistoryMapper.class, unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StaffMapper {
    // Additional mapping methods can be defined here if needed
    @Mapping(target = "avatar", ignore = true)
    Staff toEntity(StaffRequest dto);

    StaffResponse toDTO(Staff entity);

    List<Staff> toEntities(List<StaffRequest> dtos);
    List<StaffResponse> toDTOs(List<Staff> entities);
    
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "salaryHistories", ignore = true)
    void updateEntityFromDTO(StaffRequest dto, @MappingTarget Staff entity);

    ImageInfo map(ImageInfo imageInfo);

}
