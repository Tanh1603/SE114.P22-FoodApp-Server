package io.foodapp.server.repositories.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.User.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Page<Feedback> findByOrderItem_Food_Id(Long orderItemFoodId, Pageable pageable);
    // Custom query methods can be defined here if needed
}
