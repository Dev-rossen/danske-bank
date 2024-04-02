package dk.rossen.dbdemo.service;

import dk.rossen.dbdemo.model.*;
import dk.rossen.dbdemo.repository.AccountsRepository;
import dk.rossen.dbdemo.repository.PostingRepository;
import dk.rossen.dbdemo.repository.entity.AccountEntity;
import dk.rossen.dbdemo.repository.entity.PostingEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountsRepository accountsRepository;
    private final CustomerService customerService;
    private final PostingRepository postingRepository;

    public Account createAccount(NewAccount newAccount) {
        customerService.getCustomer(newAccount.customerId()).orElseThrow();

        AccountEntity accountEntity = accountsRepository.save(AccountEntity.builder()
                .accountNumber("0216" + StringUtils.leftPad(String.valueOf(new Random().nextInt(1000000000)), 10, "0"))
                .accountName(Optional.ofNullable(newAccount.accountName()).orElse("My new account"))
                .balance(0)
                .customerId(newAccount.customerId())
                .build());

        return new Account(accountEntity.getCustomerId(), accountEntity.getAccountNumber(), accountEntity.getAccountName(), accountEntity.getBalance());
    }

    public Account getAccount(String accountNumber) {
        Optional<AccountEntity> accountEntity = accountsRepository.findByAccountNumber(accountNumber);
        if (accountEntity.isPresent()) {
            return new Account(accountEntity.get().getCustomerId(), accountEntity.get().getAccountNumber(), accountEntity.get().getAccountName(), accountEntity.get().getBalance());
        }
        throw new IllegalArgumentException("Invalid account");
    }

    @Transactional
    public void deposit(AccountDeposit accountDeposit) {
        if (accountDeposit.amount() <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }

        Account account = getAccount(accountDeposit.accountNumber());
        float newBalance = account.balance() + accountDeposit.amount();
        accountsRepository.updateBalance(accountDeposit.accountNumber(), newBalance);
        postingRepository.save(PostingEntity.builder()
                .amount(accountDeposit.amount())
                .debtorAccountNumber(account.accountNumber())
                .creditorAccountNumber(account.accountNumber())
                .postingText(String.format("Deposit %s", accountDeposit.amount()))
                .balance(newBalance)
                .build());
    }

    @Transactional
    public void withdraw(AccountWithdraw accountWithdraw) {
        if (accountWithdraw.amount() <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }

        Account account = getAccount(accountWithdraw.debtorAccountNumber());
        float newBalance = account.balance() - accountWithdraw.amount();
        if (newBalance < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        accountsRepository.updateBalance(accountWithdraw.debtorAccountNumber(), newBalance);
        postingRepository.save(PostingEntity.builder()
                .amount(accountWithdraw.amount())
                .debtorAccountNumber(accountWithdraw.debtorAccountNumber())
                .creditorAccountNumber(accountWithdraw.creditorAccountNumber())
                .postingText(Optional.ofNullable(accountWithdraw.postingText()).isPresent() ? accountWithdraw.postingText() : String.format("Transfer %s from %s to %s", accountWithdraw.amount(), accountWithdraw.debtorAccountNumber(), accountWithdraw.creditorAccountNumber()))
                .balance(newBalance)
                .build());
    }

    public List<Posting> getPostings(String accountNumber) {
        Account account = getAccount(accountNumber);
        Optional<List<PostingEntity>> postingEntities = postingRepository.findPostingsByDebtorAccountNumber(account.accountNumber());
        return postingEntities.map(entities -> entities.stream().map(p -> new Posting(p.getDebtorAccountNumber(), p.getCreditorAccountNumber(), p.getAmount(), p.getPostingText(), p.getBalance())).collect(Collectors.toList())).orElse(Collections.emptyList());
    }
}
