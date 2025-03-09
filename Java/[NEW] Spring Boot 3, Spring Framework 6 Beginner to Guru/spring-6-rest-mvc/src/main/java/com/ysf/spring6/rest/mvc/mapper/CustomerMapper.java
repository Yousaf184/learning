package com.ysf.spring6.rest.mvc.mapper;

import com.ysf.spring6.rest.mvc.dto.CustomerDTO;
import com.ysf.spring6.rest.mvc.entity.Customer;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerToCustomerDTO(Customer customer);

    CustomerDTO customerDTOToCustomer(CustomerDTO customerDTO);
}
