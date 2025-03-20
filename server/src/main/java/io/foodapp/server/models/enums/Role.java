package io.foodapp.server.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    ADMIN, STAFF, SELLER, CUSTOMER;

    @JsonCreator
    public static Role fromString(String value) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Role must be one of: ADMIN, STAFF, SELLER, CUSTOMER");
    }

    @JsonValue
    public String toJson() {
        return name();
    }
}
