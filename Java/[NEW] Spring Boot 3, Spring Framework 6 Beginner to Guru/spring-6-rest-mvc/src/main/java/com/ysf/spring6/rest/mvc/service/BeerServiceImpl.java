package com.ysf.spring6.rest.mvc.service;

import com.ysf.spring6.rest.mvc.constants.BeerStyle;
import com.ysf.spring6.rest.mvc.dto.BeerDTO;
import com.ysf.spring6.rest.mvc.entity.Beer;
import com.ysf.spring6.rest.mvc.mapper.BeerMapper;
import com.ysf.spring6.rest.mvc.repository.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements IBeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    private static final int DEFAULT_MAX_PAGE_SIZE = 50;

    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Map<String, Object> paginationAndSortParams) {
        Page<Beer> beersPage;

        Pageable pageAndSortConfig = this.buildPageAndSortRequest(paginationAndSortParams);

        if ((beerName != null && !beerName.isBlank()) && beerStyle == null) {
            beersPage = this.getBeersListByName(beerName, pageAndSortConfig);
        } else if ((beerName == null || beerName.isBlank()) && beerStyle != null) {
            beersPage = this.getBeersListByBeerStyle(beerStyle, pageAndSortConfig);
        } else if ((beerName != null && !beerName.isBlank()) && beerStyle != null) {
            beersPage = this.getBeersListByNameAndStyle(beerName, beerStyle, pageAndSortConfig);
        } else {
            beersPage = this.beerRepository.findAll(pageAndSortConfig);
        }

        return beersPage.map(beerMapper::beerToBeerDTO);
    }

    private PageRequest buildPageAndSortRequest(Map<String, Object> paginationAndSortParams) {
        int pageNumber = (int) paginationAndSortParams.get("pageNum") - 1; // page numbers start from zero

        int pageSize = (int) paginationAndSortParams.get("pageSize");
        pageSize = Math.min(pageSize, DEFAULT_MAX_PAGE_SIZE);

        String sortByField = (String) paginationAndSortParams.get("sortByField");
        String sortOrder = (String) paginationAndSortParams.get("sortOrder");
        Sort sortConfig = sortOrder.equals("asc")
                ? Sort.by(sortByField).ascending()
                : Sort.by(sortByField).descending();

        return  PageRequest.of(pageNumber, pageSize, sortConfig);
    }

    private Page<Beer> getBeersListByName(String beerName, Pageable pageAndSortConfig) {
        return this.beerRepository.findByBeerNameLikeIgnoreCase("%" + beerName + "%", pageAndSortConfig);
    }

    private Page<Beer> getBeersListByBeerStyle(BeerStyle beerStyle, Pageable pageAndSortConfig) {
        return this.beerRepository.findByBeerStyle(beerStyle, pageAndSortConfig);
    }

    private Page<Beer> getBeersListByNameAndStyle(String beerName, BeerStyle beerStyle, Pageable pageAndSortConfig) {
        return this.beerRepository.findByBeerNameLikeIgnoreCaseAndBeerStyle("%" + beerName + "%", beerStyle, pageAndSortConfig);
    }

    @Cacheable(cacheNames = "beerByIdCache", key = "#id")
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
