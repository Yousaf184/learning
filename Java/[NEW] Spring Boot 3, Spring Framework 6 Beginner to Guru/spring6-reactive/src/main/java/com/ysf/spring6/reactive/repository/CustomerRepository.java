package com.ysf.spring6.reactive.repository;

import com.ysf.spring6.reactive.model.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {
}
