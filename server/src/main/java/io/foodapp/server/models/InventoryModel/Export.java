package io.foodapp.server.models.InventoryModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
@Table(name = "exports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Export {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @DateTimeFormat(pattern = "dd-MM-yyyy ")
    @Column(nullable = false)
    private LocalDate exportDate;

    @OneToMany(mappedBy = "export", cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ExportDetail> exportDetails = new ArrayList<>();
}
