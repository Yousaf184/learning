package com.ysf.spring6.rest.mvc.service;

import com.ysf.spring6.rest.mvc.dto.CustomerDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerServiceImpl implements ICustomerService {

    private final Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl() {
        CustomerDTO customerDTO1 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Customer 1")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDTO customerDTO2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Customer 2")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        CustomerDTO customerDTO3 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .name("Customer 3")
                .version(1)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        this.customerMap = new HashMap<>();
        this.customerMap.put(customerDTO1.getId(), customerDTO1);
        this.customerMap.put(customerDTO2.getId(), customerDTO2);
        this.customerMap.put(customerDTO3.getId(), customerDTO3);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return new ArrayList<>(this.customerMap.values());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID uuid) {
        return Optional.ofNullable(this.customerMap.get(uuid));
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customerDTO) {
        CustomerDTO savedCustomerDTO = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .updateDate(LocalDateTime.now())
                .createdDate(LocalDateTime.now())
                .name(customerDTO.getName())
                .build();

        this.customerMap.put(savedCustomerDTO.getId(), savedCustomerDTO);

        return savedCustomerDTO;
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID customerId, CustomerDTO customerDTO) {
        CustomerDTO existing = this.customerMap.get(customerId);

        if (existing == null) {
            return Optional.empty();
        }

        if (StringUtils.hasText(customerDTO.getName())) {
            existing.setName(customerDTO.getName());
        }

        return Optional.of(existing);
    }

    @Override
    public Optional<CustomerDTO> deleteCustomerById(UUID customerId) {
        return Optional.ofNullable(this.customerMap.remove(customerId));
    }
}