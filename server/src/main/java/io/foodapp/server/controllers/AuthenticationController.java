package io.foodapp.server.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.auth.UserRecord;

import io.foodapp.server.dtos.RegisterEmail;
import io.foodapp.server.services.FirebaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final FirebaseService firebaseService;

    /**
     * Testing the authentication get token
     */
    @PostMapping("/register")
    public ResponseEntity<UserRecord> register(@Valid @RequestBody RegisterEmail request) throws Exception {
        var response = firebaseService.createUser(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

}
