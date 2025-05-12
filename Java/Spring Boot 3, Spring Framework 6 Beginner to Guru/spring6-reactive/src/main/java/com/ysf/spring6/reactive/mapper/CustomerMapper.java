package com.ysf.spring6.reactive.mapper;

import com.ysf.spring6.reactive.dto.CustomerDTO;
import com.ysf.spring6.reactive.model.Customer;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    CustomerDTO toCustomerDto(Customer customer);

    Customer toCustomer(CustomerDTO dto);
}
