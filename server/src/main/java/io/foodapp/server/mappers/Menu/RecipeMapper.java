// package io.foodapp.server.mappers.Menu;

// import java.util.List;
// import java.util.stream.Collectors;

// import org.mapstruct.AfterMapping;
// import org.mapstruct.BeanMapping;
// import org.mapstruct.Context;
// import org.mapstruct.Mapper;
// import org.mapstruct.Mapping;
// import org.mapstruct.MappingTarget;
// import org.mapstruct.ReportingPolicy;

// import io.foodapp.server.dtos.Menu.RecipeDetailResponse;
// import io.foodapp.server.dtos.Menu.RecipeRequest;
// import io.foodapp.server.dtos.Menu.RecipeResponse;
// import io.foodapp.server.models.MenuModel.Recipe;
// import io.foodapp.server.models.MenuModel.RecipeDetail;
// import io.foodapp.server.repositories.Menu.IngredientRepository;
// import io.foodapp.server.repositories.Menu.MenuItemRepository;
// import io.foodapp.server.repositories.Menu.RecipeDetailRepository;

// @Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {RecipeDetailMapper.class })
// public interface RecipeMapper {
//     Recipe toEntity(RecipeRequest dto,
//             @Context MenuItemRepository menuItemRepository,
//             @Context RecipeDetailMapper recipeDetailMapper,
//             @Context RecipeDetailRepository recipeDetailRepository,
//             @Context IngredientRepository ingredientRepository);

//     @Mapping(target = "deleted", source = "deleted")
//     RecipeResponse toDTO(Recipe entity);

//     List<Recipe> toEntities(List<RecipeRequest> dtos);

//     List<RecipeResponse> toDTOs(List<Recipe> entities);

//     @BeanMapping(ignoreByDefault = false)
//     void updateEntityFromDto(RecipeRequest dto, @MappingTarget Recipe entity,
//             @Context MenuItemRepository menuItemRepository,
//             @Context RecipeDetailMapper recipeDetailMapper,
//             @Context RecipeDetailRepository recipeDetailRepository,
//             @Context IngredientRepository ingredientRepository);

//     @AfterMapping
//     default void setRelatedEntities(RecipeRequest dto, @MappingTarget Recipe entity,
//             @Context MenuItemRepository menuItemRepository,
//             @Context RecipeDetailMapper recipeDetailMapper,
//             @Context RecipeDetailRepository recipeDetailRepository,
//             @Context IngredientRepository ingredientRepository) {

//         if (dto.getRecipeDetails() != null) {
//             entity.setRecipeDetails(dto.getRecipeDetails().stream().map(item -> {
//                 RecipeDetail upsert;
//                 if (item.getId() == null) {
//                     upsert = recipeDetailMapper.toEntity(item, entity, ingredientRepository);
//                 } else {
//                     upsert = recipeDetailRepository.findById(item.getId())
//                             .orElseThrow(() -> new RuntimeException("Order item not found for ID: " + item.getId()));
//                     recipeDetailMapper.updateEntityFromDto(item, upsert, entity, ingredientRepository);
//                 }
//                 return upsert;
//             }).collect(Collectors.toList()));
//         }

//         entity.setMenuItem(menuItemRepository.findById(dto.getMenuItemId())
//                 .orElseThrow(() -> new RuntimeException("Menu item not found for ID: " + dto.getMenuItemId())));
//     }

//     @AfterMapping
//     default void handleToResponse(@MappingTarget RecipeResponse dto, Recipe entity) {
//         if (dto.getRecipeDetails() != null) {
//             List<RecipeDetailResponse> filteredItems = dto.getRecipeDetails().stream()
//                     .filter(item -> !item.isDeleted())
//                     .toList();

//             dto.setRecipeDetails(filteredItems);
//         }
//     }
// }
