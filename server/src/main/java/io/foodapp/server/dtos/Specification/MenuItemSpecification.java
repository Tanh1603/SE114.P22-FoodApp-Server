package io.foodapp.server.dtos.Specification;

import org.springframework.data.jpa.domain.Specification;

import io.foodapp.server.dtos.Filter.MenuItemFilter;
import io.foodapp.server.models.MenuModel.MenuItem;

public class MenuItemSpecification {
    public static Specification<MenuItem> withFilter(MenuItemFilter filter) {
        return Specification.where(hasMenuId(filter.getMenuId()))
                .and(isAvailable(filter.getIsAvailable()));
    }
    

    private static Specification<MenuItem> hasMenuId(Long menuId) {
        return (root, query, criteriaBuilder) -> {
            if (menuId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("menu").get("id"), menuId);
        };
    }

    private static Specification<MenuItem> isAvailable(Boolean isAvailable) {
        return (root, query, cb) -> {
            if (isAvailable == null) {
                return cb.conjunction();
            }
            return isAvailable
                    ? cb.isTrue(root.get("isAvailable"))
                    : cb.isFalse(root.get("isAvailable"));
        };
    }
    
}
