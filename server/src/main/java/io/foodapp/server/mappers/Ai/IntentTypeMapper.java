package io.foodapp.server.mappers.Ai;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import io.foodapp.server.dtos.Ai.IntentTypeRequest;
import io.foodapp.server.dtos.Ai.IntentTypeResponse;
import io.foodapp.server.models.AiModel.IntentType;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE, unmappedSourcePolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface IntentTypeMapper {
    IntentType toEntity(IntentTypeRequest dto);

    IntentTypeResponse toDTO(IntentType entity);

    List<IntentType> toEntities(List<IntentTypeRequest> dtos);

    List<IntentTypeResponse> toDTOs(List<IntentType> entities);

    @BeanMapping(ignoreByDefault = false)
    void updateEntityFromDto(IntentTypeRequest intentTypeRequest, @MappingTarget IntentType intentType);
}
