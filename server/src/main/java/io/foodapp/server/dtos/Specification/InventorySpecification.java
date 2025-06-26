package io.foodapp.server.dtos.Specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import io.foodapp.server.dtos.Filter.InventoryFilter;
import io.foodapp.server.models.InventoryModel.Inventory;

public class InventorySpecification {

    public static Specification<Inventory> withFilter(InventoryFilter filter) {
        return Specification.where(hasIngredientName(filter.getIngredientName()))
                .and(isOutOfStock(filter.getIsOutOfStock()))
                .and(isExpired(filter.getIsExpired()));
    }

    private static Specification<Inventory> hasIngredientName(String ingredientName) {
        return (root, query, cb) -> {
            if (ingredientName == null || ingredientName.isBlank()) {
                return cb.conjunction();
            }
            return cb.like(
                cb.lower(root.get("ingredient").get("name")),
                "%" + ingredientName.toLowerCase() + "%"
            );
        };
    }


    private static Specification<Inventory> isExpired(Boolean isExpired) {
        return (root, query, cb) -> {
            if (isExpired == null) {
                return cb.conjunction();
            }
            if (isExpired) {
                // Hết hạn: expiryDate < hôm nay
                return cb.lessThan(root.get("expiryDate"), LocalDate.now());
            } else {
                // Chưa hết hạn: expiryDate >= hôm nay
                return cb.greaterThanOrEqualTo(root.get("expiryDate"), LocalDate.now());
            }
        };
    }


    private static Specification<Inventory> isOutOfStock(Boolean isOutOfStock) {
        return (root, query, cb) -> {
            if (isOutOfStock == null) {
                return cb.conjunction();
            }
            return isOutOfStock
                    ? cb.isTrue(root.get("isOutOfStock"))
                    : cb.isFalse(root.get("isOutOfStock"));
        };
    }
}
