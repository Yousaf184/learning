package com.ysf.spring6.rest.mvc.event;

import com.ysf.spring6.rest.mvc.entity.Beer;
import lombok.*;
import org.springframework.security.core.Authentication;

@Builder
public record BeerCreatedEvent(Beer createdBeer, Authentication auth) {
}
