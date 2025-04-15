package io.foodapp.server.models.User;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.foodapp.server.models.enums.VoucherType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "vouchers")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID code;
    private double value;

    @Column(nullable = true)
    private Double minOrderValue;

    @Column(nullable = true)
    private Double maxOrderValue;

    private int total;

    @Enumerated(EnumType.STRING)
    private VoucherType type;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;

    @JsonProperty("isActive")
    private boolean isActive;
    
}