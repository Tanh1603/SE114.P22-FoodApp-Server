package io.foodapp.server.dtos.Specification;

import org.springframework.data.jpa.domain.Specification;

import io.foodapp.server.dtos.Filter.AppNotificationFilter;
import io.foodapp.server.models.Order.AppNotification;

public class AppNotificationSpecification {
    public static Specification<AppNotification> withFilter(AppNotificationFilter filter) {
        return Specification.where(hasRead(filter.getIsRead()))
                .and(hasUserId(filter.getUserId()));
    }

    private static Specification<AppNotification> hasRead(Boolean read) {
        return (root, query, criteriaBuilder) -> {
            if (read == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("isRead"), read);
        };
    }

    private static Specification<AppNotification> hasUserId(String userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("userId"), userId);
        };
    }
}
