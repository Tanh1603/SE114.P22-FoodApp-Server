package io.foodapp.server.dtos.Filter;

import lombok.Data;

@Data
public class AppNotificationFilter {
    private String userId;
    private Boolean isRead;
}
