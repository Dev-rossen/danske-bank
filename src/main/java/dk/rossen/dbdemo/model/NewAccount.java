package dk.rossen.dbdemo.model;

import java.util.Objects;

public record NewAccount(String customerId, String accountName) {
    public NewAccount {
        Objects.requireNonNull(customerId, "Invalid customer id");
    }
}
