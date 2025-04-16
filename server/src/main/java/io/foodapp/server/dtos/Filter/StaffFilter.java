package io.foodapp.server.dtos.Filter;

import io.foodapp.server.models.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffFilter {
    private String fullName;
    private Gender gender;
}
