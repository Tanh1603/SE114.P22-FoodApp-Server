package io.foodapp.server.models.InventoryModel;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.foodapp.server.models.MenuModel.Ingredient;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventorys")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ingredient_id", nullable = false)
    @JsonBackReference
    private Ingredient ingredient;

    private LocalDate expiryDate;
    private LocalDate productionDate;
    private BigDecimal cost;
    private BigDecimal quantityImported;
    private BigDecimal quantityRemaining;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isDeleted;
}
