package io.foodapp.server.configs;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.foodapp.server.models.User;
import io.foodapp.server.models.enums.Role;
import io.foodapp.server.repositories.UserRepository;
import io.foodapp.server.services.JwtService;
import io.foodapp.server.services.RedisService;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final UserRepository repository;
    private final RedisService redisService;
    private final JwtService jwtService;

    @Bean
    public UserDetailsService userDetailsService() throws UsernameNotFoundException {
        return username -> repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username `" + username + "` not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @SuppressWarnings("unused")
    @Bean
    public CommandLineRunner commandLineRunner() {
        return arg -> {
            List<User> admin = repository.findByRole(Role.ADMIN);
            if (admin.isEmpty() || admin == null) {
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder().encode("123"))
                        .fullName("TANH")
                        .email("admin@gmail.com")
                        .role(Role.ADMIN)
                        .build();
                repository.save(user);

                var accessToken = jwtService.generateAccessToken(user);
                var refreshToken = jwtService.generateRefreshToken();

                redisService.storeAccessToken(user.getUsername(), accessToken);
                redisService.storeRefreshToken(user.getUsername(), refreshToken);
            }
        };
    }
}
