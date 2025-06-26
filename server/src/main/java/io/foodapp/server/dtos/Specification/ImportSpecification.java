package io.foodapp.server.dtos.Specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import io.foodapp.server.dtos.Filter.ImportFilter;
import io.foodapp.server.models.InventoryModel.Import;

public class ImportSpecification {
    public static Specification<Import> withFilter(ImportFilter filter) {
        return Specification.where(hasSupplierName(filter.getSupplierName()))
                .and(hasBetweenDate(filter.getStartDate(), filter.getEndDate()));
    }


    private static Specification<Import> hasSupplierName(String supplierName) {
        return (root, query, criteriaBuilder) -> {
            if (supplierName == null || supplierName.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("supplier").get("name")),
                "%" + supplierName.toLowerCase() + "%"
            );
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
