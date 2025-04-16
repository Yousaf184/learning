package com.ysf.spring6.reactive.repository;

import com.ysf.spring6.reactive.model.Beer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeerRepository extends ReactiveCrudRepository<Beer, Integer> {
}
