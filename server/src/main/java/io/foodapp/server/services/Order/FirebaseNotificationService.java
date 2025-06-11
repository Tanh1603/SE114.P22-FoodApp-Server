package io.foodapp.server.services.Order;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import io.foodapp.server.dtos.Filter.AppNotificationFilter;
import io.foodapp.server.dtos.Notification.OrderNotification;
import io.foodapp.server.dtos.Specification.AppNotificationSpecification;
import io.foodapp.server.models.Order.AppNotification;
import io.foodapp.server.repositories.Order.AppNotificationRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FirebaseNotificationService {
    private final FirebaseMessaging firebaseMessaging;
    private final AppNotificationRepository notificationRepository;

    public void sendNotification(String userId, OrderNotification request) {
        try {
            notificationRepository.save(AppNotification.builder()
                    .userId(userId)
                    .title(request.getTitle())
                    .body(request.getBody())
                    .build());

            Message message = Message.builder()
                    .setToken(request.getToken())
                    .setNotification(Notification.builder()
                            .setTitle(request.getTitle())
                            .setBody(request.getBody())
                            .build())
                    .build();

            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            System.err.println("FCM error: " + e.getMessage());
            throw new RuntimeException("FCM error: " + e.getMessage());
        }
    }

    public List<AppNotification> getAppNotification(AppNotificationFilter filter) {
        Specification<AppNotification> specification = AppNotificationSpecification.withFilter(filter);
        return notificationRepository.findAll(specification);
    }

    public void readAppNotification(Long id) {
        AppNotification exist = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found for id: " + id));
        exist.setRead(true);
        notificationRepository.save(exist);
    }
}
