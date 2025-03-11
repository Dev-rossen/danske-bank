package dk.rossen.dbdemo.controller;

import dk.rossen.dbdemo.model.*;
import dk.rossen.dbdemo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/v1/accounts")
@RequiredArgsConstructor
public class AccountsController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody NewAccount newAccount) {
        try {
            return ResponseEntity.ok(accountService.createAccount(newAccount));
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid customer", e);
        }
    }

    @GetMapping(path = "/{account-id}")
    public ResponseEntity<Account> getAccount(@PathVariable(name = "account-id") String accountId) {
        try {
            return ResponseEntity.ok(accountService.getAccount(accountId));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(path = "/{account-id}/deposit")
    public ResponseEntity<Void> deposit(@RequestBody @Validated AccountDeposit accountDeposit, @PathVariable(name = "account-id") String accountId) {
        if (!accountId.equalsIgnoreCase(accountDeposit.accountNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input");
        }

        try {
            accountService.deposit(accountDeposit);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{account-id}/withdraw")
    public ResponseEntity<Void> withdraw(@RequestBody @Validated AccountWithdraw accountWithdraw, @PathVariable(name = "account-id") String accountId) {
        if (!accountId.equalsIgnoreCase(accountWithdraw.debtorAccountNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid input");
        }

        try {
            accountService.withdraw(accountWithdraw);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{account-id}/postings")
    public ResponseEntity<List<Posting>> getPostings(@PathVariable(name = "account-id") String accountId) {
        return ResponseEntity.ok(accountService.getPostings(accountId));
    }
}
