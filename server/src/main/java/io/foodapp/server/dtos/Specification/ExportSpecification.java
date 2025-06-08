package io.foodapp.server.dtos.Specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import io.foodapp.server.dtos.Filter.ExportFilter;
import io.foodapp.server.models.InventoryModel.Export;

public class ExportSpecification {
    public static Specification<Export> withFilter(ExportFilter filter) {
        return Specification.where(hasBetweenDate(filter.getStartDate(), filter.getEndDate()));
    }



    private static Specification<Export> hasBetweenDate(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return criteriaBuilder.conjunction();
            }
            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("exportDate"), startDate, endDate);
            }
            if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("exportDate"), startDate);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("exportDate"), endDate);

        };

    }
}
