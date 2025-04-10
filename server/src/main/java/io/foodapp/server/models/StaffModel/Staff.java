package io.foodapp.server.models.StaffModel;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.SQLRestriction;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.foodapp.server.models.enums.Gender;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "staffs")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String position;
    private String phone;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private String address;
    private String imageUrl;
    private LocalDate birthDate;
    private LocalDate startDate;
    private LocalDate endDate;

    private double basicSalary;

    @JsonProperty("isDeleted")
    private boolean isDeleted;

    @SQLRestriction("is_deleted = false")
    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SalaryHistory> salaryHistories;
}
