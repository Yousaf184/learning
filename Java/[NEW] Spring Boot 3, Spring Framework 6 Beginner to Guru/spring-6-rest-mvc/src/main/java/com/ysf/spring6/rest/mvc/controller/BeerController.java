package com.ysf.spring6.rest.mvc.controller;

import com.ysf.spring6.rest.mvc.exceptions.NotFoundException;
import com.ysf.spring6.rest.mvc.model.Beer;
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
    public ResponseEntity<List<Beer>> listBeers() {
        return ResponseEntity.ok(this.beerService.listBeers());
    }

    @GetMapping("/{beerId}")
    public ResponseEntity<Beer> getBeerById(@PathVariable("beerId") UUID beerId) {
        Optional<Beer> beerOptional = this.beerService.getBeerById(beerId);
        Beer beer = beerOptional.orElseThrow(BeerController.beerNotFoundExSupplier);

        return ResponseEntity.ok(beer);
    }

    @PostMapping("/")
    public ResponseEntity<Beer> saveNewBeer(@RequestBody Beer beer) {
        Beer createdBeer = this.beerService.saveNewBeer(beer);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdBeer);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity<Beer> updateBeerById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {
        Optional<Beer> beerOptional = this.beerService.updateBeerById(beerId, beer);
        Beer updatedBeer = beerOptional.orElseThrow(BeerController.beerNotFoundExSupplier);

        return ResponseEntity.ok(updatedBeer);
    }

    @PatchMapping("/{beerId}")
    public ResponseEntity<Beer> patchBeerById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {
        Optional<Beer> beerOptional = this.beerService.patchBeerById(beerId, beer);
        Beer patchedBeer = beerOptional.orElseThrow(BeerController.beerNotFoundExSupplier);

        return ResponseEntity.ok(patchedBeer);
    }

    @DeleteMapping("/{beerId}")
    public ResponseEntity<Beer> deleteBeerById(@PathVariable("beerId") UUID beerId) {
        Optional<Beer> beerOptional = this.beerService.deleteBeerById(beerId);
        Beer removedBeer = beerOptional.orElseThrow(BeerController.beerNotFoundExSupplier);

        return ResponseEntity.ok(removedBeer);
    }
}