package io.foodapp.server.dtos.Specification;

import org.springframework.data.jpa.domain.Specification;
import io.foodapp.server.dtos.Filter.StaffFilter;
import io.foodapp.server.models.StaffModel.Staff;
import io.foodapp.server.models.enums.Gender;

public class StaffSpecification {

    public static Specification<Staff> withFilter(StaffFilter filter) {
        return Specification.where(hasName(filter.getFullName()))
                .and(hasGender(filter.getGender()))
                .and(isActive(filter.getActive()));
    }

    private static Specification<Staff> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), "%" + name.toLowerCase() + "%");
        };
    }

    private static Specification<Staff> hasGender(Gender gender) {
        return (root, query, criteriaBuilder) -> {
            if (gender == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("gender"), gender);
        };
    }


    private static Specification<Staff> isActive(Boolean active) {
        return (root, query, criteriaBuilder) -> {
            if (active == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("active"), active);
        };
    }

}
