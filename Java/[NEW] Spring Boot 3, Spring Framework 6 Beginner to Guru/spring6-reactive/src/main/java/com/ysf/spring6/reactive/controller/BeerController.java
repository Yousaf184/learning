package com.ysf.spring6.reactive.controller;

import com.ysf.spring6.reactive.dto.BeerDTO;
import com.ysf.spring6.reactive.service.IBeerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/reactive/v1/beer")
@RequiredArgsConstructor
public class BeerController {

    private final IBeerService beerService;

    @GetMapping(value = {"", "/"})
    public Flux<BeerDTO> getAllBeers() {
        return this.beerService.getAllBeers();
    }

    @GetMapping("/{beerId}")
    public Mono<BeerDTO> getBeerById(@PathVariable("beerId") Integer beerId) {
        return this.beerService.getBeerById(beerId);
    }

    @PostMapping(value = {"", "/"})
    public Mono<ResponseEntity<BeerDTO>> createNewBeer(@Valid @RequestBody BeerDTO newBeerDto) {
        return this.beerService
                .createNewBeer(newBeerDto)
                .map(savedBeerDTO -> new ResponseEntity<>(savedBeerDTO, HttpStatus.CREATED));
    }

    @PutMapping("/{beerId}")
    public Mono<BeerDTO> updateBeerById(
            @PathVariable("beerId") Integer beerId,
            @RequestBody BeerDTO updateBeerDTO
    ) {
        return this.beerService.updateBeerById(beerId, updateBeerDTO);
    }

    @DeleteMapping("/{beerId}")
    public Mono<ResponseEntity<Void>> deleteBeerById(@PathVariable("beerId") Integer beerId) {
        return this.beerService
                .deleteBeerById(beerId)
                .map(voidResponse -> ResponseEntity.noContent().build());
    }
}
