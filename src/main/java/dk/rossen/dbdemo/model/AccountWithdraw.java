package dk.rossen.dbdemo.model;

public record AccountWithdraw(
        String debtorAccountNumber,
        String creditorAccountNumber,
        float amount,
        String postingText
) {
}
