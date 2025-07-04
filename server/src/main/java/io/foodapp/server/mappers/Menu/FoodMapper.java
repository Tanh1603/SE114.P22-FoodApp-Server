package io.foodapp.server.mappers.Menu;

import io.foodapp.server.dtos.Menu.FoodRequest;
import io.foodapp.server.dtos.Menu.FoodResponse;
import io.foodapp.server.models.MenuModel.Food;
import io.foodapp.server.utils.ImageInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface FoodMapper {

    Food toEntity(FoodRequest dto);
    FoodResponse toDTO(Food entity);

    List<FoodResponse> toDTOs(List<Food> entities);
    List<Food> toEntities(List<FoodRequest> dtos);

    @Mapping(target = "images", ignore = true)
    void updateEntityFromDTO(@MappingTarget Food entity, FoodRequest dto);

    ImageInfo map(ImageInfo imageInfo);
}
