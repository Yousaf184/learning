package com.ysf.spring6.reactive.service;

import com.ysf.spring6.reactive.dto.BeerDTO;
import com.ysf.spring6.reactive.mapper.BeerMapper;
import com.ysf.spring6.reactive.model.Beer;
import com.ysf.spring6.reactive.repository.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements IBeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Flux<BeerDTO> getAllBeers() {
        return this.beerRepository
                .findAll()
                .map(this.beerMapper::toBeerDTO);
    }

    @Override
    public Mono<BeerDTO> getBeerById(Integer beerId) {
        return this.beerRepository
                .findById(beerId)
                .map(this.beerMapper::toBeerDTO);
    }

    @Override
    public Mono<BeerDTO> createNewBeer(BeerDTO newBeerDto) {
        Beer beer = this.beerMapper.toBeer(newBeerDto);
        return this.beerRepository
                .save(beer)
                .map(this.beerMapper::toBeerDTO);
    }

    @Override
    public Mono<BeerDTO> updateBeerById(Integer beerId, BeerDTO updateBeerDTO) {
        return this.beerRepository
                .findById(beerId)
                .map(beer -> mapUpdatedDTOFields(beer, updateBeerDTO))
                .flatMap(this.beerRepository::save)
                .map(this.beerMapper::toBeerDTO);
    }

    private Beer mapUpdatedDTOFields(Beer existingBeer, BeerDTO updateBeerDTO) {
        if (StringUtils.hasText(updateBeerDTO.getBeerName())) {
            existingBeer.setBeerName(updateBeerDTO.getBeerName());
        }
        if (StringUtils.hasText(updateBeerDTO.getBeerStyle())) {
            existingBeer.setBeerStyle(updateBeerDTO.getBeerStyle());
        }
        if (StringUtils.hasText(updateBeerDTO.getUpc())) {
            existingBeer.setUpc(updateBeerDTO.getUpc());
        }
        if (updateBeerDTO.getQuantityOnHand() != null) {
            existingBeer.setQuantityOnHand(updateBeerDTO.getQuantityOnHand());
        }
        if (updateBeerDTO.getPrice() != null) {
            existingBeer.setPrice(updateBeerDTO.getPrice());
        }
        return existingBeer;
    }

    @Override
    public Mono<Void> deleteBeerById(Integer beerId) {
        return this.beerRepository.deleteById(beerId);
    }
}
