package io.foodapp.server.dtos.Specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import io.foodapp.server.models.InventoryModel.Inventory;
import io.foodapp.server.dtos.Filter.InventoryFilter;

public class InventorySpecification {

    public static Specification<Inventory> withFilter(InventoryFilter filter) {
        return Specification.where(hasIngredientId(filter.getIngredientId()))
                .and(isOutOfStock(filter.getIsOutOfStock()))
                .and(hasExpiryDate(filter.getExpiryDate()));
    }

    private static Specification<Inventory> hasIngredientId(Long ingredientId) {
        return (root, query, cb) -> {
            if (ingredientId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("ingredient").get("id"), ingredientId);
        };
    }

    private static Specification<Inventory> hasExpiryDate(LocalDate expiryDate) {
        return (root, query, cb) -> {
            if (expiryDate == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("expiryDate"), expiryDate);
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
