package io.foodapp.server.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.foodapp.server.dtos.requests.LoginRequest;
import io.foodapp.server.dtos.requests.RefreshTokenRequest;
import io.foodapp.server.dtos.requests.RegisterRequest;
import io.foodapp.server.dtos.responses.ApiResponse;
import io.foodapp.server.dtos.responses.AuthenticationResponse;
import io.foodapp.server.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@Valid @RequestBody LoginRequest request) {
        var response = authService.login(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Login Success")
                .data(response)
                .build();
    }

    @PostMapping("/register")
    public ApiResponse<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        var response = authService.register(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Register Success")
                .data(response)
                .build();
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/logout")
    public ApiResponse logout(@RequestHeader("access-token") String request) {
        boolean response = authService.logout(request);
        return ApiResponse.builder()
                .status(response ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value())
                .message(response ? "Logout success" : "Logout failed")
                .build();
    }

    @PostMapping("/refresh-token")
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        var response = authService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .status(HttpStatus.OK.value())
                .message(response != null ? "Refresh token success" : "Refresh token failed")
                .data(response)
                .build();

    }
}
