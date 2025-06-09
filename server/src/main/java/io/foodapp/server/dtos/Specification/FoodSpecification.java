package io.foodapp.server.dtos.Specification;

import org.springframework.data.jpa.domain.Specification;

import io.foodapp.server.dtos.Filter.FoodFilter;
import io.foodapp.server.models.MenuModel.Food;

public class FoodSpecification {
    public static Specification<Food> withFilter(FoodFilter filter) {
        return Specification.where(hasStatus(filter.getStatus()))
                .and(hasName(filter.getName()))
                .and(hasMenuId(filter.getMenuId()));
    }

    private static Specification<Food> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%");
        };
    }

    private static Specification<Food> hasStatus(Boolean active) {
        return (root, query, criteriaBuilder) -> {
            if (active == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("active"), active);
        };
    }

    private static Specification<Food> hasMenuId(Integer menuId) {
        return (root, query, cb) -> {
            if (menuId == null)
                return null;
            return cb.equal(root.get("menu").get("id"), menuId);
        };
    }

}
