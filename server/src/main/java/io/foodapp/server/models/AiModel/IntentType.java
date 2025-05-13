package io.foodapp.server.models.AiModel;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "intent_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "intentType", cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ChatKnowledgeEntry> chatKnowledgeEntrys = new ArrayList<>();
}
