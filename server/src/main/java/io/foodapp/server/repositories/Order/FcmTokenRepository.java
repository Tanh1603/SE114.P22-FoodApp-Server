package io.foodapp.server.repositories.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.Order.FcmToken;

import java.util.List;
import java.util.Optional;

import io.foodapp.server.models.enums.UserType;


@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    Optional<FcmToken> findByUserIdAndUserType(String userId, UserType userType);
    
    Optional<FcmToken> findByUserType(UserType userType);

    List<FcmToken> findAllByUserType(UserType userType);

    void deleteByUserIdAndUserType(String userId, UserType userType);
}
