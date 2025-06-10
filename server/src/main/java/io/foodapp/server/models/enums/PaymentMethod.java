package io.foodapp.server.models.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    CASH("Cash"),
    BANK("Bank");

    private final String method;

    PaymentMethod(String method) {
        this.method = method;
    }

}
