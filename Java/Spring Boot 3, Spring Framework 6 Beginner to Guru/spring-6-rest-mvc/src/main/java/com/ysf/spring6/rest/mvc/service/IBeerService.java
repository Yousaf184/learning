package com.ysf.spring6.rest.mvc.service;

import com.ysf.spring6.rest.mvc.constants.BeerStyle;
import com.ysf.spring6.rest.mvc.dto.BeerDTO;
import org.springframework.data.domain.Page;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface IBeerService {

    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Map<String, Object> paginationAndSortParams);

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO saveNewBeer(BeerDTO beerDTO);

    Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beerDTO);

    Optional<BeerDTO> deleteBeerById(UUID beerId);
}