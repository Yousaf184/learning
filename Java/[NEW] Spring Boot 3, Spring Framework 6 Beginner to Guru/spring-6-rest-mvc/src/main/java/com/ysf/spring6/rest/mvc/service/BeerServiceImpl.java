package com.ysf.spring6.rest.mvc.service;

import com.ysf.spring6.rest.mvc.dto.BeerDTO;
import com.ysf.spring6.rest.mvc.entity.Beer;
import com.ysf.spring6.rest.mvc.mapper.BeerMapper;
import com.ysf.spring6.rest.mvc.repository.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements IBeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public List<BeerDTO> listBeers(){
        return this.beerRepository.findAll()
                .stream()
                .map(this.beerMapper::beerToBeerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        Optional<Beer> beerOptional = this.beerRepository.findById(id);
        Beer beer = beerOptional.orElse(null);

        BeerDTO beerDTO = this.beerMapper.beerToBeerDTO(beer);

        return Optional.ofNullable(beerDTO);
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beerDTO) {
        if (beerDTO.getId() != null) {
            beerDTO.setId(null);
        }

        if (beerDTO.getVersion() != null) {
            beerDTO.setVersion(null);
        }

        beerDTO.setCreatedDate(LocalDateTime.now());
        beerDTO.setUpdateDate(LocalDateTime.now());

        Beer beerToSave = this.beerMapper.beerDTOToBeer(beerDTO);
        Beer savedBeer = this.beerRepository.save(beerToSave);

        return this.beerMapper.beerToBeerDTO(savedBeer);
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beerDTO) {
        Optional<Beer> beerOptional = this.beerRepository.findById(beerId);

        if (beerOptional.isEmpty() || !beerId.equals(beerDTO.getId())) {
            return Optional.empty();
        }

        Beer beerToUpdate = beerOptional.orElse(null);

        if (StringUtils.hasText(beerDTO.getBeerName())){
            beerToUpdate.setBeerName(beerDTO.getBeerName());
        }

        if (beerDTO.getBeerStyle() != null) {
            beerToUpdate.setBeerStyle(beerDTO.getBeerStyle());
        }

        if (beerDTO.getPrice() != null) {
            beerToUpdate.setPrice(beerDTO.getPrice());
        }

        if (beerDTO.getQuantityOnHand() != null){
            beerToUpdate.setQuantityOnHand(beerDTO.getQuantityOnHand());
        }

        if (StringUtils.hasText(beerDTO.getUpc())) {
            beerToUpdate.setUpc(beerDTO.getUpc());
        }

        beerToUpdate.setUpdateDate(LocalDateTime.now());

        Beer updatedBeer = this.beerRepository.save(beerToUpdate);

        BeerDTO updatedBeerDTO = this.beerMapper.beerToBeerDTO(updatedBeer);
        return Optional.of(updatedBeerDTO);
    }

    @Override
    public Optional<BeerDTO> deleteBeerById(UUID beerId) {
        Optional<Beer> beerOptional = this.beerRepository.findById(beerId);

        if (beerOptional.isEmpty()) {
            return Optional.empty();
        }

        this.beerRepository.deleteById(beerId);

        Beer deletedBeer = beerOptional.orElse(null);
        BeerDTO deletedBeerDTO = this.beerMapper.beerToBeerDTO(deletedBeer);

        return Optional.ofNullable(deletedBeerDTO);
    }
}
