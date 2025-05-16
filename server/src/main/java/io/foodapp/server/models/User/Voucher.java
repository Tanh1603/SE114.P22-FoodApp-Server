package io.foodapp.server.models.User;

import java.time.LocalDate;

import jakarta.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import io.foodapp.server.models.enums.VoucherType;
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

    @Column(unique = true)
    private String code;

    private double value;
    private Double minOrderPrice;
    private Double maxValue;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private VoucherType type;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;

}