package com.ysf.spring6.rest.mvc.repository;

import com.ysf.spring6.rest.mvc.entity.BeerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BeerOrderRepository extends JpaRepository<BeerOrder, UUID> {
}