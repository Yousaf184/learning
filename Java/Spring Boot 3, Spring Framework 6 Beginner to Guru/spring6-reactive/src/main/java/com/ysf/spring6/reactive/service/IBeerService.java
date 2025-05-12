package com.ysf.spring6.reactive.service;

import com.ysf.spring6.reactive.dto.BeerDTO;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBeerService {

    Flux<BeerDTO> getAllBeers();

    Mono<BeerDTO> getBeerById(Integer beerId);

    Mono<BeerDTO> createNewBeer(BeerDTO newBeerDto);

    Mono<BeerDTO> updateBeerById(Integer beerId, BeerDTO updateBeerDTO);

    Mono<Void> deleteBeerById(Integer beerId);
}
