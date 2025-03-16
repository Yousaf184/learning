package com.ysf.spring6.rest.mvc.service;

import com.ysf.spring6.rest.mvc.constants.BeerStyle;
import com.ysf.spring6.rest.mvc.dto.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IBeerService {

    List<BeerDTO> listBeers(String beerName, BeerStyle beerStyle);

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO saveNewBeer(BeerDTO beerDTO);

    Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beerDTO);

    Optional<BeerDTO> deleteBeerById(UUID beerId);
}