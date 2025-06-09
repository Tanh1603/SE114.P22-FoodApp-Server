package io.foodapp.server.dtos.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import io.foodapp.server.dtos.Filter.OrderFilter;
import io.foodapp.server.models.Order.Order;
import io.foodapp.server.models.enums.OrderStatus;
import io.foodapp.server.models.enums.PaymentMethod;
import io.foodapp.server.models.enums.ServingType;

public class OrderSpecification {
    public static Specification<Order> withFilter(OrderFilter filter) {
        return Specification.where(hasStatus(filter.getStatus()))
                .and(hasPaymentMethod(filter.getMethod()))
                .and(hasCustomerId(filter.getCustomerId()))
                .and(hasSellerId(filter.getSellerId()))
                .and(hasShipperId((filter.getShipperId())))
                .and(hasServingType((filter.getType())))
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

    private static Specification<Order> hasServingType(ServingType type) {
        return (root, query, criteriaBuilder) -> {
            if (type == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("type"), type);
        };
    }

    private static Specification<Order> hasCustomerId(String customerId) {
        return (root, query, criteriaBuilder) -> {
            return (customerId != null && !customerId.isBlank())
                    ? criteriaBuilder.equal(root.get("customerId"), customerId)
                    : criteriaBuilder.conjunction();
        };
    }

    private static Specification<Order> hasSellerId(String sellerId) {
        return (root, query, criteriaBuilder) -> {
            return (sellerId != null && !sellerId.isBlank()) ? criteriaBuilder.equal(root.get("sellerId"),
                    sellerId)
                    : criteriaBuilder.conjunction();
        };
    }

    private static Specification<Order> hasShipperId(String shipperId) {
        return (root, query, criteriaBuilder) -> {
            return (shipperId != null && !shipperId.isBlank()) ? criteriaBuilder.equal(root.get("shipperId"),
                    shipperId)
                    : criteriaBuilder.conjunction();
        };
    }

    private static Specification<Order> hasPaymentMethod(PaymentMethod paymentMethod) {
        return (root, query, criteriaBuilder) -> {
            if (paymentMethod == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("method"), paymentMethod);
        };
    }

    private static Specification<Order> hasBetweenDate(LocalDate startDate, LocalDate endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return criteriaBuilder.conjunction();
            }
            if (startDate != null && endDate != null) {
                LocalDateTime startDateTime = startDate.atStartOfDay();
                LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
                return criteriaBuilder.between(root.get("startedAt"), startDateTime, endDateTime);
            }
            if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("startedAt"), startDate.atStartOfDay());
            }
            if (endDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("startedAt"), endDate.atTime(23, 59, 59));
            }
            return criteriaBuilder.conjunction();
        };

    }

}
