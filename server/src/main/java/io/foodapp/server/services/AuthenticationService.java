package io.foodapp.server.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import io.foodapp.server.dtos.requests.LoginRequest;
import io.foodapp.server.dtos.requests.RefreshTokenRequest;
import io.foodapp.server.dtos.requests.RegisterRequest;
import io.foodapp.server.dtos.responses.AuthenticationResponse;
import io.foodapp.server.mappers.StaffRequestMapper;
import io.foodapp.server.mappers.UserMapper;
import io.foodapp.server.models.Staff;
import io.foodapp.server.models.User;
import io.foodapp.server.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager manager;
    private final JwtService jwtService;
    private final RedisService redisService;
    private final UserMapper userMapper;
    private final StaffRequestMapper staffRequestMapper;
    private final PasswordEncoder encoder;

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username `" + request.getUsername() + "` has exsit");
        }

        User user = User.builder().role(request.getRole()).username(request.getUsername())
                .password(encoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .build();
        if (request.getStaff() != null) {
            Staff staff = staffRequestMapper.toEntity(request.getStaff());
            staff.setAccount(user);
            user.setStaff(staff);
        }
        
        var newUser = userRepository.save(user);

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken();

        redisService.storeAccessToken(newUser.getUsername(), accessToken);
        redisService.storeRefreshToken(newUser.getUsername(), refreshToken);

        var response = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(userMapper.toDTO(newUser))
                .build();
        return response;
    }

    public AuthenticationResponse login(LoginRequest request) throws BadCredentialsException {
        try {
            manager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));
            var user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow();

            var accessToken = jwtService.generateAccessToken(user);
            var refreshToken = jwtService.generateRefreshToken();

            redisService.storeAccessToken(user.getUsername(), accessToken);
            redisService.storeRefreshToken(user.getUsername(), refreshToken);
            return AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username or password", e);
        }
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request)
            throws IllegalArgumentException {
        var username = request.getUsername();
        if (!userRepository.findByUsername(username).isPresent()) {
            throw new BadCredentialsException("Username `" + request.getUsername() + "` not found");
        }

        if (redisService.isTokenBlacklisted(request.getRefreshToken())
                || redisService.getRefreshToken(request.getUsername()) == null) {
            throw new IllegalArgumentException("Invalid or expirated refresh token");
        }

        if (!redisService.getRefreshToken(username).equals(request.getRefreshToken())) {
            throw new IllegalArgumentException("Invalid or expirated refresh token");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException(username + " not found"));

        var newAccessToken = jwtService.generateAccessToken(user);
        redisService.storeAccessToken(username, newAccessToken);

        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(request.getRefreshToken())
                .build();
    }

    public boolean logout(String token) throws IllegalArgumentException {
        String username = jwtService.extractUsername(token);
        if (!token.equals(redisService.getAccessToken(username))) {
            throw new IllegalArgumentException("Invalid access token");
        }

        String refreshToken = redisService.getRefreshToken(username);
        String accessToken = redisService.getAccessToken(username);

        redisService.blacklistToken(accessToken);
        redisService.blacklistToken(refreshToken);

        return true;
    }
}
