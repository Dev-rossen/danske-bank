package dk.rossen.dbdemo.model;

public record Posting(String debtorAccountNumber, String creditorAccountNumber, float amount, String postingText, float balance) {
}
