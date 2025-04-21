package io.foodapp.server.models.InventoryModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

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
@Table(name = "import_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "import_id", nullable = false)
    @JsonBackReference
    private Import anImport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    @JsonBackReference
    private Ingredient ingredient;


    private LocalDateTime expiryDate;


    private LocalDateTime productionDate;
    private BigDecimal quantity;
    private BigDecimal cost;

    @Column(name = "is_deleted")
    @JsonProperty("isDeleted")
    private boolean isDeleted;
}