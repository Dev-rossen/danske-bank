package dk.rossen.dbdemo.model;

import java.util.Objects;

public record NewCustomer(String firstName, String lastName, String customerId) {
    public NewCustomer {
        Objects.requireNonNull(firstName, "First name cannot be empty");
        Objects.requireNonNull(lastName, "Last name cannot be empty");
        Objects.requireNonNull(customerId, "SSN cannot be empty");
    }
}
