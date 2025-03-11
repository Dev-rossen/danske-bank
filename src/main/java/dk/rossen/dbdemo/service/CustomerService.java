package dk.rossen.dbdemo.service;

import dk.rossen.dbdemo.model.Account;
import dk.rossen.dbdemo.model.Customer;
import dk.rossen.dbdemo.model.NewCustomer;
import dk.rossen.dbdemo.repository.AccountsRepository;
import dk.rossen.dbdemo.repository.CustomerRepository;
import dk.rossen.dbdemo.repository.entity.AccountEntity;
import dk.rossen.dbdemo.repository.entity.CustomerEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AccountsRepository accountsRepository;

    public Customer createCustomer(@Valid NewCustomer newCustomer) {
        if (customerRepository.findByCustomerId(newCustomer.customerId()).isPresent()) {
            throw new IllegalArgumentException("Customer already exists");
        }

        CustomerEntity customerEntity = customerRepository.save(CustomerEntity.builder()
                .firstName(newCustomer.firstName())
                .lastName(newCustomer.lastName())
                .customerId(newCustomer.customerId())
                .build());
        return new Customer(customerEntity.getCustomerId(), customerEntity.getFirstName(), customerEntity.getLastName(), null);
    }

    public Optional<Customer> getCustomer(String id) {
        Optional<CustomerEntity> customerEntity = customerRepository.findByCustomerId(id);
        customerEntity.orElseThrow();

        Optional<List<AccountEntity>> accountEntityList = accountsRepository.findAllByCustomerId(id);
        List<Account> accountList = null;
        if (accountEntityList.isPresent()) {
            accountList = accountEntityList.get().stream().map(a -> new Account(a.getCustomerId(), a.getAccountNumber(), a.getAccountName(), a.getBalance())).collect(Collectors.toList());
        }

        return Optional.of(new Customer(customerEntity.get().getCustomerId(), customerEntity.get().getFirstName(), customerEntity.get().getLastName(), accountList));
    }
}
