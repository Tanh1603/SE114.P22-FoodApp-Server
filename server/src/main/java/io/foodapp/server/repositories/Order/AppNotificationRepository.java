package io.foodapp.server.repositories.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import io.foodapp.server.models.Order.AppNotification;
import java.util.List;

public interface AppNotificationRepository
        extends JpaRepository<AppNotification, Long>, JpaSpecificationExecutor<AppNotification> {
    List<AppNotification> findByUserId(String userId);
    List<AppNotification> findByUserIdAndIsReadFalse(String userId);
}
