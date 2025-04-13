package io.foodapp.server.repositories.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.Order.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    // Custom query methods can be defined here if needed
    
}
