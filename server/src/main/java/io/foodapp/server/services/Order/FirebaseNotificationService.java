package io.foodapp.server.services.Order;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import io.foodapp.server.dtos.Notification.OrderNotification;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FirebaseNotificationService {
    private final FirebaseMessaging firebaseMessaging;

    public void sendNotification(OrderNotification request) {
        try {
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
}
