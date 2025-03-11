package dk.rossen.dbdemo.model;

public record Account(
        String customerId,
        String accountNumber,
        String accountName,
        float balance
) {
}
