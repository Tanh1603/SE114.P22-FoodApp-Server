package io.foodapp.server.models.enums;

public enum PaymentMethod {
    CASH("Cash"),
    BANK("Bank");

    private final String method;

    PaymentMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
