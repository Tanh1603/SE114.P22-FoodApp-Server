package io.foodapp.server.dtos.Specification;

import org.springframework.data.jpa.domain.Specification;

import io.foodapp.server.dtos.Filter.FoodTableFilter;
import io.foodapp.server.models.Order.FoodTable;

public class FoodTableSpecification {
    public static Specification<FoodTable> withFilter(FoodTableFilter filter) {
        return Specification.where(hasActive(filter.getActive())).and(hasStatus(filter.getStatus()));
    }

    private static Specification<FoodTable> hasActive(Boolean active) {
        return (root, query, criteriaBuilder) -> {
            if (active == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("active"), active);
        };
    }

    public static Specification<FoodTable> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null || status.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }
}
