package com.ysf.spring6.reactive.mapper;

import com.ysf.spring6.reactive.dto.BeerDTO;
import com.ysf.spring6.reactive.model.Beer;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    BeerDTO toBeerDTO(Beer beer);

    Beer toBeer(BeerDTO beerDTO);
}
