package io.foodapp.server.repositories.Ai;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.foodapp.server.models.AiModel.ChatKnowledgeEntry;

@Repository
public interface ChatKnowledgeEntryRepository extends JpaRepository<ChatKnowledgeEntry, Long> {

}
