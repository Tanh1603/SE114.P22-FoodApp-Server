package io.foodapp.server.dtos.Specification;

import org.springframework.data.jpa.domain.Specification;

import io.foodapp.server.dtos.Filter.SupplierFilter;
import io.foodapp.server.models.InventoryModel.Supplier;

public class SupplierSpecification {

    public static Specification<Supplier> withFilter(SupplierFilter filter) {
        return Specification.where(hasName(filter.getName()))
                .and(hasPhone(filter.getPhone()))
                .and(hasEmail(filter.getEmail()))
                .and(hasAddress(filter.getAddress()))
                .and(isActive(filter.getIsActive()));
    }

    private static Specification<Supplier> hasName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    private static Specification<Supplier> hasPhone(String phone) {
        return (root, query, cb) -> {
            if (phone == null || phone.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("phone")), "%" + phone.toLowerCase() + "%");
        };
    }

    private static Specification<Supplier> hasEmail(String email) {
        return (root, query, cb) -> {
            if (email == null || email.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
        };
    }

    private static Specification<Supplier> hasAddress(String address) {
        return (root, query, cb) -> {
            if (address == null || address.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.like(cb.lower(root.get("address")), "%" + address.toLowerCase() + "%");
        };
    }

    private static Specification<Supplier> isActive(Boolean isActive) {
        return (root, query, cb) -> {
            if (isActive == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("isActive"), isActive);
        };
    }
}
