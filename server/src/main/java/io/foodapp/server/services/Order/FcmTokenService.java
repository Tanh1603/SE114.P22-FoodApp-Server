package io.foodapp.server.services.Order;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.foodapp.server.models.Order.FcmToken;
import io.foodapp.server.models.enums.UserType;
import io.foodapp.server.repositories.Order.FcmTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FcmTokenService {
    private final FcmTokenRepository fcmRepository;

    public void saveOrUpdateToken(String userId, UserType userType, String fcmToken) {
        Optional<FcmToken> existingToken = fcmRepository
                .findByUserIdAndUserType(userId, userType);

        if (existingToken.isPresent()) {
            FcmToken token = existingToken.get();
            token.setToken(fcmToken);
            fcmRepository.save(token);
        } else {
            FcmToken newToken = FcmToken.builder()
                    .userId(userId)
                    .userType(userType)
                    .token(fcmToken)
                    .build();
            fcmRepository.save(newToken);
        }
    }

    public FcmToken getFcmToken(String userId, UserType userType) {
        return fcmRepository.findByUserIdAndUserType(userId, userType)
                .orElseThrow(() -> new RuntimeException("FcmToken not found"));
    }

    public FcmToken _getFcmTokenByType(UserType type) {
        return fcmRepository.findByUserType(type).orElseThrow(() -> new RuntimeException("FcmToken not found"));
    }

    public List<FcmToken> getAllFcmTokenByType(UserType type) {
        return fcmRepository.findAllByUserType(type);
    }

    public void removeToken(String userId, UserType userType) {
        fcmRepository.deleteByUserIdAndUserType(userId, userType);
    }
}
