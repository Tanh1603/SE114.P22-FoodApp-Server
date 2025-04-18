package io.foodapp.server.models.InventoryModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLRestriction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.foodapp.server.models.StaffModel.Staff;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "imports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Import {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    @JsonBackReference
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    @JsonBackReference
    private Staff staff;

    
    @Column(nullable = false)
    private LocalDateTime importDate;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @SQLRestriction("is_deleted = false")
    @OneToMany(mappedBy = "anImport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ImportDetail> importDetails = new ArrayList<>();
}
