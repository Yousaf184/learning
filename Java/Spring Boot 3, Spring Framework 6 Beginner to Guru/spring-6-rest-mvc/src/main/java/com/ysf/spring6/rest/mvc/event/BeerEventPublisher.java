package com.ysf.spring6.rest.mvc.event;

import com.ysf.spring6.rest.mvc.entity.Beer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BeerEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publishBeerCreatedEvent(Beer createdBeer, Authentication auth) {
        BeerCreatedEvent beerCreatedEvent = BeerCreatedEvent.builder()
                .createdBeer(createdBeer)
                .auth(auth)
                .build();
        this.eventPublisher.publishEvent(beerCreatedEvent);
    }
}
