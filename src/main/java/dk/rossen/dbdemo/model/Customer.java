package dk.rossen.dbdemo.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record Customer(
        String customerId,
        String firstName,
        String lastName,
        @JsonInclude(JsonInclude.Include.NON_NULL) List<Account> accountList
) {
}
