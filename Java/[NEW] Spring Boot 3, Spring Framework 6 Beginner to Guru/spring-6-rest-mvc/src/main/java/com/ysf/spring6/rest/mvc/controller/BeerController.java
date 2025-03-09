package com.ysf.spring6.rest.mvc.controller;

import com.ysf.spring6.rest.mvc.exceptions.NotFoundException;
import com.ysf.spring6.rest.mvc.dto.BeerDTO;
import com.ysf.spring6.rest.mvc.service.IBeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    @GetMapping("/")
    public ResponseEntity<List<BeerDTO>> listBeers() {
        return ResponseEntity.ok(this.beerService.listBeers());
    }

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDTO> getBeerById(@PathVariable("beerId") UUID beerId) {
        Optional<BeerDTO> beerOptional = this.beerService.getBeerById(beerId);
        BeerDTO beerDTO = beerOptional.orElseThrow(BeerController.beerNotFoundExSupplier);

        return ResponseEntity.ok(beerDTO);
    }

    @PostMapping("/")
    public ResponseEntity<BeerDTO> saveNewBeer(@RequestBody BeerDTO beerDTO) {
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

    @PatchMapping("/{beerId}")
    public ResponseEntity<BeerDTO> patchBeerById(@PathVariable("beerId") UUID beerId, @RequestBody BeerDTO beerDTO) {
        Optional<BeerDTO> beerOptional = this.beerService.patchBeerById(beerId, beerDTO);
        BeerDTO patchedBeerDTO = beerOptional.orElseThrow(BeerController.beerNotFoundExSupplier);

        return ResponseEntity.ok(patchedBeerDTO);
    }

    @DeleteMapping("/{beerId}")
    public ResponseEntity<BeerDTO> deleteBeerById(@PathVariable("beerId") UUID beerId) {
        Optional<BeerDTO> beerOptional = this.beerService.deleteBeerById(beerId);
        BeerDTO removedBeerDTO = beerOptional.orElseThrow(BeerController.beerNotFoundExSupplier);

        return ResponseEntity.ok(removedBeerDTO);
    }
}