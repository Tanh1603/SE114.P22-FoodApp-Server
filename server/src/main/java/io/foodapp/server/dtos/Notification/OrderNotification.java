package io.foodapp.server.dtos.Notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderNotification {
    private String type;
    private Long orderId;
    private String message;
    private Object data;
}
