package com.ysf.spring6.rest.mvc.repository;

import com.ysf.spring6.rest.mvc.constants.BeerStyle;
import com.ysf.spring6.rest.mvc.entity.Beer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BeerRepository extends JpaRepository<Beer, UUID> {

    Page<Beer> findByBeerNameLikeIgnoreCase(String beerName, Pageable pageable);

    Page<Beer> findByBeerStyle(BeerStyle beerStyle, Pageable pageable);

    Page<Beer> findByBeerNameLikeIgnoreCaseAndBeerStyle(String beerName, BeerStyle beerStyle, Pageable pageable);
}
