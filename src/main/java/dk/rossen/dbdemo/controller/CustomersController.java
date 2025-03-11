package dk.rossen.dbdemo.controller;

import dk.rossen.dbdemo.model.Customer;
import dk.rossen.dbdemo.model.NewCustomer;
import dk.rossen.dbdemo.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/v1/customers")
@RequiredArgsConstructor
public class CustomersController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody NewCustomer newCustomer) {
        Customer customer;
        try {
            customer = customerService.createCustomer(newCustomer);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        return ResponseEntity.ok(customer);
    }

    @GetMapping(path = "/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable(name = "customerId") String customerId) {
        Optional<Customer> customer = customerService.getCustomer(customerId);
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
