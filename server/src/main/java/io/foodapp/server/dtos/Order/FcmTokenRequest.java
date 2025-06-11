package io.foodapp.server.dtos.Order;

import io.foodapp.server.models.enums.UserType;
import lombok.Data;

@Data
public class FcmTokenRequest {
    private String userId;
    private UserType userType;
    private String token;
}
