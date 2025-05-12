package com.ysf.spring6.rest.mvc.converter;

import com.ysf.spring6.rest.mvc.constants.BeerStyle;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BeerStyleConverter implements AttributeConverter<BeerStyle, String> {

    @Override
    public String convertToDatabaseColumn(BeerStyle beerStyle) {
        return beerStyle.name();
    }

    @Override
    public BeerStyle convertToEntityAttribute(String dbData) {
        return BeerStyle.valueOf(dbData);
    }
}
