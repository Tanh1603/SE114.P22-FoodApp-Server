package io.foodapp.server.controllers.Order;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.foodapp.server.dtos.Order.FcmTokenRequest;
import io.foodapp.server.services.Order.FcmTokenService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final FcmTokenService fcmTokenService;

    @PostMapping("/register-token")
    public ResponseEntity<String> registerToken(@RequestBody FcmTokenRequest request) {
        fcmTokenService.saveOrUpdateToken(
                request.getUserId(),
                request.getUserType(),
                request.getToken());
        return ResponseEntity.ok("Token registered successfully");

    }

    @DeleteMapping("/remove-token")
    public ResponseEntity<String> removeToken(@RequestBody FcmTokenRequest request) {

        fcmTokenService.removeToken(request.getUserId(), request.getUserType());
        return ResponseEntity.ok("Token removed successfully");

    }
}
