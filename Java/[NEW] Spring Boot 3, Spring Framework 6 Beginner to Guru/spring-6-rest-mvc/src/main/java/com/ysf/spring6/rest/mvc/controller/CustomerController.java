package com.ysf.spring6.rest.mvc.controller;

import com.ysf.spring6.rest.mvc.exceptions.NotFoundException;
import com.ysf.spring6.rest.mvc.dto.CustomerDTO;
import com.ysf.spring6.rest.mvc.service.ICustomerService;
import jakarta.validation.Valid;
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

    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<CustomerDTO>> listAllCustomers() {
        return ResponseEntity.ok(this.customerService.getAllCustomers());
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable("customerId") UUID customerId) {
        Optional<CustomerDTO> customerOptional = this.customerService.getCustomerById(customerId);
        CustomerDTO customerDTO = customerOptional.orElseThrow(CustomerController.customerNotFoundExSupplier);

        return ResponseEntity.ok(customerDTO);
    }

    @PostMapping(value = {"", "/"})
    public ResponseEntity<CustomerDTO> saveNewCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        CustomerDTO createdCustomerDTO = this.customerService.saveNewCustomer(customerDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdCustomerDTO);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> updateCustomerById(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDTO customerDTO) {
        Optional<CustomerDTO> customerOptional = this.customerService.updateCustomerById(customerId, customerDTO);
        CustomerDTO updatedCustomerDTO = customerOptional.orElseThrow(CustomerController.customerNotFoundExSupplier);

        return ResponseEntity.ok(updatedCustomerDTO);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<CustomerDTO> deleteCustomerById(@PathVariable("customerId") UUID customerId) {
        Optional<CustomerDTO> customerOptional = this.customerService.deleteCustomerById(customerId);
        CustomerDTO removedCustomerDTO = customerOptional.orElseThrow(CustomerController.customerNotFoundExSupplier);

        return ResponseEntity.ok(removedCustomerDTO);
    }
}