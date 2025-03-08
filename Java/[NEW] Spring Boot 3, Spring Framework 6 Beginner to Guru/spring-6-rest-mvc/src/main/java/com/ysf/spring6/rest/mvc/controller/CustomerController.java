package com.ysf.spring6.rest.mvc.controller;

import com.ysf.spring6.rest.mvc.exceptions.NotFoundException;
import com.ysf.spring6.rest.mvc.model.Customer;
import com.ysf.spring6.rest.mvc.service.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final ICustomerService customerService;

    private static final Supplier<NotFoundException> customerNotFoundExSupplier = () -> {
        String errorMsg = "Couldn't find the customer with the given id";
        return new NotFoundException(errorMsg);
    };

    @GetMapping("/")
    public ResponseEntity<List<Customer>> listAllCustomers() {
        return ResponseEntity.ok(this.customerService.getAllCustomers());
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("customerId") UUID customerId) {
        Optional<Customer> customerOptional = this.customerService.getCustomerById(customerId);
        Customer customer = customerOptional.orElseThrow(CustomerController.customerNotFoundExSupplier);

        return ResponseEntity.ok(customer);
    }

    @PostMapping("/")
    public ResponseEntity<Customer> saveNewCustomer(@RequestBody Customer customer) {
        Customer createdCustomer = this.customerService.saveNewCustomer(customer);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdCustomer);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> updateCustomerById(@PathVariable("customerId") UUID customerId, @RequestBody Customer customer) {
        Optional<Customer> customerOptional = this.customerService.updateCustomerById(customerId, customer);
        Customer updatedCustomer = customerOptional.orElseThrow(CustomerController.customerNotFoundExSupplier);

        return ResponseEntity.ok(updatedCustomer);
    }

    @PatchMapping("/{customerId}")
    public ResponseEntity<Customer> patchCustomerById(@PathVariable("customerId") UUID customerId, @RequestBody Customer customer) {
        Optional<Customer> customerOptional = this.customerService.patchCustomerById(customerId, customer);
        Customer patchedCustomer = customerOptional.orElseThrow(CustomerController.customerNotFoundExSupplier);

        return ResponseEntity.ok(patchedCustomer);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Customer> deleteCustomerById(@PathVariable("customerId") UUID customerId) {
        Optional<Customer> customerOptional = this.customerService.deleteCustomerById(customerId);
        Customer removedCustomer = customerOptional.orElseThrow(CustomerController.customerNotFoundExSupplier);

        return ResponseEntity.ok(removedCustomer);
    }
}