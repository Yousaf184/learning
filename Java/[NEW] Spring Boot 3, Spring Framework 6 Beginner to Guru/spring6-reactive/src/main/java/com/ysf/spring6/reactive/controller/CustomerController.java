package com.ysf.spring6.reactive.controller;

import com.ysf.spring6.reactive.dto.CustomerDTO;
import com.ysf.spring6.reactive.service.ICustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/reactive/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final ICustomerService customerService;

    @GetMapping(value = {"", "/"})
    public Flux<CustomerDTO> getAllCustomer() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{customerId}")
    public Mono<CustomerDTO> getCustomerById(@PathVariable("customerId") Integer customerId) {
        return this.customerService
                .getCustomerById(customerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @PostMapping(value = {"", "/"})
    public Mono<ResponseEntity<CustomerDTO>> createNewCustomer(
            @Valid @RequestBody CustomerDTO customerDTO
    ) {
        return this.customerService
                .createNewCustomer(customerDTO)
                .map(savedCustomerDTO -> new ResponseEntity<>(savedCustomerDTO, HttpStatus.CREATED));
    }

    @PutMapping("/{customerId}")
    public Mono<CustomerDTO> updateCustomerById(
            @PathVariable("customerId") Integer customerId,
            @RequestBody CustomerDTO updateCustomerDTO
    ) {
        return this.customerService
                .updateCustomerById(customerId, updateCustomerDTO)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @DeleteMapping("/{customerId}")
    Mono<ResponseEntity<Void>> deleteCustomerById(@PathVariable("customerId") Integer customerId) {
        return this.customerService
                .deleteCustomerById(customerId)
                .map(voidResponse -> ResponseEntity.noContent().build());
    }
}
