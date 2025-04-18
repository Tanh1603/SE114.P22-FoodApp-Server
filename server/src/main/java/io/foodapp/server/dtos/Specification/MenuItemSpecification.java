package io.foodapp.server.dtos.Specification;

import org.springframework.data.jpa.domain.Specification;

import io.foodapp.server.dtos.Filter.MenuItemFilter;
import io.foodapp.server.models.MenuModel.MenuItem;

public class MenuItemSpecification {
    public static Specification<MenuItem> withFilter(MenuItemFilter filter) {
        return Specification.where(hasMenuId(filter.getMenuId()))
                .and(isAvailable(filter.isAvailable()));
    }

    private static Specification<MenuItem> hasMenuId(Long menuId) {
        return (root, query, criteriaBuilder) -> {
            if (menuId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("menu").get("id"), menuId);
        };
    }

    private static Specification<MenuItem> isAvailable(boolean isAvailable) {
        return (root, query, criteriaBuilder) -> {
            if (isAvailable) {
                return criteriaBuilder.isTrue(root.get("isAvailable"));
            } else {
                return criteriaBuilder.isFalse(root.get("isAvailable"));
            }
        };
    }
}
