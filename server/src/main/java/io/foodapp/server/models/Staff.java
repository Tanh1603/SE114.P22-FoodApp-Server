package io.foodapp.server.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Staffs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Staff {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User account;

    private String position;

    private LocalDate birthDate;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Column(columnDefinition = "DECIMAL(18,2) DEFAULT 0")
    private BigDecimal basicSalary = BigDecimal.ZERO;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isDeleted = false;
}
