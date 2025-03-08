package com.ysf.spring6.rest.mvc.service;

import com.ysf.spring6.rest.mvc.model.Customer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerServiceImpl implements ICustomerService {

    private final Map<UUID, Customer> customerMap;

    public CustomerServiceImpl() {
        Customer customer1 = Customer.builder()
                .id(UUID.randomUUID())
                .name("Customer 1")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .id(UUID.randomUUID())
                .name("Customer 2")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Customer customer3 = Customer.builder()
                .id(UUID.randomUUID())
                .name("Customer 3")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        this.customerMap = new HashMap<>();
        this.customerMap.put(customer1.getId(), customer1);
        this.customerMap.put(customer2.getId(), customer2);
        this.customerMap.put(customer3.getId(), customer3);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(this.customerMap.values());
    }

    @Override
    public Optional<Customer> getCustomerById(UUID uuid) {
        return Optional.ofNullable(this.customerMap.get(uuid));
    }

    @Override
    public Customer saveNewCustomer(Customer customer) {
        Customer savedCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .updateDate(LocalDateTime.now())
                .createdDate(LocalDateTime.now())
                .name(customer.getName())
                .build();

        this.customerMap.put(savedCustomer.getId(), savedCustomer);

        return savedCustomer;
    }

    @Override
    public Optional<Customer> updateCustomerById(UUID customerId, Customer customer) {
        Customer existing = this.customerMap.get(customerId);

        if (existing == null) {
            return Optional.empty();
        }

        existing.setName(customer.getName());

        return Optional.of(existing);
    }

    @Override
    public Optional<Customer> patchCustomerById(UUID customerId, Customer customer) {
        Customer existing = this.customerMap.get(customerId);

        if (existing == null) {
            return Optional.empty();
        }

        if (StringUtils.hasText(customer.getName())) {
            existing.setName(customer.getName());
        }

        return Optional.of(existing);
    }

    @Override
    public Optional<Customer> deleteCustomerById(UUID customerId) {
        return Optional.ofNullable(this.customerMap.remove(customerId));
    }
}