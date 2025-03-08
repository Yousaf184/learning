package com.ysf.spring6.rest.mvc.service;

import com.ysf.spring6.rest.mvc.model.Beer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IBeerService {

    List<Beer> listBeers();

    Optional<Beer> getBeerById(UUID id);

    Beer saveNewBeer(Beer beer);

    Optional<Beer> updateBeerById(UUID beerId, Beer beer);

    Optional<Beer> patchBeerById(UUID beerId, Beer beer);

    Optional<Beer> deleteBeerById(UUID beerId);
}