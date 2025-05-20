package com.ysf.accounts.mapper;

import com.ysf.accounts.dto.CustomerDto;
import com.ysf.accounts.entity.Customer;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer toCustomerEntity(CustomerDto customerDto);

    CustomerDto toCustomerDto(Customer customer);
}
