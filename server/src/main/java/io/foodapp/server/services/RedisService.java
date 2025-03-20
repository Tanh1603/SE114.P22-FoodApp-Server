package io.foodapp.server.services;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {
    private static final long TOKEN_EXPIRATION = Duration.ofHours(1).toMillis();
    private static final long REFRESH_EXPIRATION = Duration.ofDays(7).toMillis();

    private final RedisTemplate<String, String> redisTemplate;

    private static final String ACCESS_TOKEN_KEY = "access-token:";
    private static final String REFRESH_TOKEN_KEY = "refresh-token:";
    private static final String BLACKLIST_KEY = "jwt:blacklist:";

    // hanle refresh token
    public String getRefreshToken(String username) {
        return redisTemplate.opsForValue().get(REFRESH_TOKEN_KEY + username);
    }

    public void storeRefreshToken(String username, String refreshToken) {
        String oldRefreshToken = getRefreshToken(username);
        if (oldRefreshToken != null) {
            blacklistToken(oldRefreshToken);
        }
        redisTemplate.opsForValue().set(REFRESH_TOKEN_KEY + username, refreshToken, REFRESH_EXPIRATION,
                TimeUnit.MILLISECONDS);
    }

    // handle access token
    public String getAccessToken(String username) {
        return redisTemplate.opsForValue().get(ACCESS_TOKEN_KEY + username);
    }

    public void storeAccessToken(String username, String newAccessToken) {
        String oldAccessToken = getAccessToken(username);
        if (oldAccessToken != null) {
            blacklistToken(oldAccessToken);
        }
        redisTemplate.opsForValue().set(ACCESS_TOKEN_KEY + username, newAccessToken,
                TOKEN_EXPIRATION,
                TimeUnit.MILLISECONDS);
    }

    public void blacklistToken(String token) {
        redisTemplate.opsForValue().set(BLACKLIST_KEY + token, "blacklisted", TOKEN_EXPIRATION, TimeUnit.MILLISECONDS);
    }

    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_KEY + token));
    }

    public void deleteRefreshToken(String username) {
        redisTemplate.delete(REFRESH_TOKEN_KEY + username);
    }
}
