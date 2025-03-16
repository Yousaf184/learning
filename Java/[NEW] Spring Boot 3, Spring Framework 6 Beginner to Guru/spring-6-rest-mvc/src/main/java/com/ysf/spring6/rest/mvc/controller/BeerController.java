package com.ysf.spring6.rest.mvc.controller;

import com.ysf.spring6.rest.mvc.constants.BeerStyle;
import com.ysf.spring6.rest.mvc.exceptions.NotFoundException;
import com.ysf.spring6.rest.mvc.dto.BeerDTO;
import com.ysf.spring6.rest.mvc.service.IBeerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Supplier;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {
    private final IBeerService beerService;

    private static final Supplier<NotFoundException> beerNotFoundExSupplier = () -> {
        String errorMsg = "Couldn't find the beer with the given id";
        return new NotFoundException(errorMsg);
    };

    @GetMapping(value = {"", "/"})
    public ResponseEntity<Map<String, Object>> listBeers(
        @RequestParam(name = "beerName", required = false) String beerName,
        @RequestParam(name = "beerStyle", required = false) BeerStyle beerStyle,
        @RequestParam(name = "page", required = false, defaultValue = "1") int pageNum,
        @RequestParam(name = "pageSize", required = false, defaultValue = "5") int pageSize,
        @RequestParam(name = "sortByField", required = false, defaultValue = "beerName") String sortByField,
        @RequestParam(name = "sortOrder", required = false, defaultValue = "asc") String sortOrder
    ) {
        Map<String, Object> paginationAndSortParams = new HashMap<>();
        paginationAndSortParams.put("pageNum", pageNum);
        paginationAndSortParams.put("pageSize", pageSize);
        paginationAndSortParams.put("sortByField", sortByField);
        paginationAndSortParams.put("sortOrder", sortOrder);

        Page<BeerDTO> paginatedBeerData = this.beerService.listBeers(beerName, beerStyle, paginationAndSortParams);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("data", paginatedBeerData.getContent());
        responseMap.put("currentRecordCount", paginatedBeerData.getNumberOfElements());
        responseMap.put("currentPageNumber", pageNum);
        responseMap.put("pageSize", paginatedBeerData.getSize());
        responseMap.put("totalPageCount", paginatedBeerData.getTotalPages());
        responseMap.put("totalRowCount", paginatedBeerData.getTotalElements());
        responseMap.put("sortKey", sortByField);
        responseMap.put("sortOrder", sortOrder);

        return ResponseEntity.ok(responseMap);
    }

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDTO> getBeerById(@PathVariable("beerId") UUID beerId) {
        Optional<BeerDTO> beerOptional = this.beerService.getBeerById(beerId);
        BeerDTO beerDTO = beerOptional.orElseThrow(BeerController.beerNotFoundExSupplier);

        return ResponseEntity.ok(beerDTO);
    }

    @PostMapping(value = {"", "/"})
    public ResponseEntity<BeerDTO> saveNewBeer(@Valid @RequestBody BeerDTO beerDTO) {
        BeerDTO createdBeerDTO = this.beerService.saveNewBeer(beerDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdBeerDTO);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity<BeerDTO> updateBeerById(@PathVariable("beerId") UUID beerId, @RequestBody BeerDTO beerDTO) {
        Optional<BeerDTO> beerOptional = this.beerService.updateBeerById(beerId, beerDTO);
        BeerDTO updatedBeerDTO = beerOptional.orElseThrow(BeerController.beerNotFoundExSupplier);

        return ResponseEntity.ok(updatedBeerDTO);
    }

    @DeleteMapping("/{beerId}")
    public ResponseEntity<BeerDTO> deleteBeerById(@PathVariable("beerId") UUID beerId) {
        Optional<BeerDTO> beerOptional = this.beerService.deleteBeerById(beerId);
        BeerDTO removedBeerDTO = beerOptional.orElseThrow(BeerController.beerNotFoundExSupplier);

        return ResponseEntity.ok(removedBeerDTO);
    }
}