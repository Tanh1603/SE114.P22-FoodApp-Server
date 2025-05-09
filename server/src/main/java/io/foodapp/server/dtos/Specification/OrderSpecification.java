package io.foodapp.server.dtos.Specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import io.foodapp.server.dtos.Filter.OrderFilter;
import io.foodapp.server.models.Order.Order;
import io.foodapp.server.models.enums.OrderStatus;
import io.foodapp.server.models.enums.PaymentMethod;

public class OrderSpecification {
    public static Specification<Order> withFilter(OrderFilter filter) {
        return Specification.where(hasStatus(filter.getStatus()))
                .and(hasPaymentMethod(filter.getPaymentMethod()))
                .and(hasBetweenDate(filter.getStartDate(), filter.getEndDate()));
    }


    private static Specification<Order> hasStatus(OrderStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    private static Specification<Order> hasPaymentMethod(PaymentMethod paymentMethod) {
        return (root, query, criteriaBuilder) -> {
            if (paymentMethod == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("paymentMethod"), paymentMethod);
        };
    }

    private static Specification<Order> hasBetweenDate(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return criteriaBuilder.conjunction();
            }
            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("orderDate"), startDate, endDate);
            }
            if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("orderDate"), startDate);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("orderDate"), endDate);

        };

    }

}
