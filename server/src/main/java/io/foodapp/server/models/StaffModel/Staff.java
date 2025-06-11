package io.foodapp.server.models.StaffModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import io.foodapp.server.utils.ImageInfo;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.foodapp.server.models.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

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


    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private ImageInfo avatar;

    private String address;
    private LocalDate birthDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private double basicSalary;

    @Builder.Default
    private boolean active = true;

    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<SalaryHistory> salaryHistories = new ArrayList<>();
}
