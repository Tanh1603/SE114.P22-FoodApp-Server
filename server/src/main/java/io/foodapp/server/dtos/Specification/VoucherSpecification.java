package io.foodapp.server.dtos.Specification;

import io.foodapp.server.dtos.Filter.VoucherFilter;
import io.foodapp.server.models.User.Voucher;
import io.foodapp.server.models.enums.VoucherType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class VoucherSpecification {
    public static Specification<Voucher> withFilter(VoucherFilter filter) {
        return Specification.where(withType(filter.getType()))
                .and(withTotalBetween(filter.getMinQuantity(), filter.getMaxQuantity()))
                .and(withStartDateGreaterThanOrEqualTo(filter.getStartDate()))
                .and(withEndDateLessThanOrEqualTo(filter.getEndDate()));
    }

    private static Specification<Voucher> withTotalBetween(Integer minTotal, Integer maxTotal) {
        return (root, query, criteriaBuilder) -> {
            if (minTotal != null && maxTotal != null) {
                return criteriaBuilder.between(root.get("total"), minTotal, maxTotal);
            } else if (minTotal != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("total"), minTotal);
            } else if (maxTotal != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("total"), maxTotal);
            }
            return null;
        };
    }

    private static Specification<Voucher> withType(VoucherType type) {
        return (root, query, criteriaBuilder) -> {
            if (type == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("type"), type);
        };
    }

    private static Specification<Voucher> withStartDateGreaterThanOrEqualTo(LocalDate startDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null) {
                return null;
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startDate);
        };
    }

    private static Specification<Voucher> withEndDateLessThanOrEqualTo(LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (endDate == null) {
                return null;
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endDate);
        };
    }

}
