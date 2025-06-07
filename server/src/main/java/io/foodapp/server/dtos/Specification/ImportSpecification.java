package io.foodapp.server.dtos.Specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import io.foodapp.server.dtos.Filter.ImportFilter;
import io.foodapp.server.models.InventoryModel.Import;

public class ImportSpecification {
    public static Specification<Import> withFilter(ImportFilter filter) {
        return Specification.where(hasSupplierId(filter.getSupplierId()))
                .and(hasBetweenDate(filter.getStartDate(), filter.getEndDate()));
    }


    private static Specification<Import> hasSupplierId(Long supplierId) {
        return (root, query, criteriaBuilder) -> {
            if (supplierId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("supplier").get("id"), supplierId);
        };
    }

    private static Specification<Import> hasBetweenDate(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return criteriaBuilder.conjunction();
            }
            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("importDate"), startDate, endDate);
            }
            if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("importDate"), startDate);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("importDate"), endDate);

        };

    }
}
