package io.foodapp.server.repositories.Ai;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.AiModel.IntentType;

@Repository
public interface IntentTypeRepository extends JpaRepository<IntentType, Integer>{

}
