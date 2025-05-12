package com.ysf.spring6.rest.mvc.event;

import com.ysf.spring6.rest.mvc.entity.Beer;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class BeerEventListener {

    @EventListener
    @Async
    public void onBeerCreatedEvent(BeerCreatedEvent beerCreatedEvent) {
        Beer createdBeer = beerCreatedEvent.createdBeer();
        String name = beerCreatedEvent.auth().getName();

        System.out.println("BEER CREATED EVENT:");
        System.out.println("-------------------");
        System.out.println("Created Beer Id:" + createdBeer.getId());
        System.out.println("Created By:" + name);
        System.out.println("-------------------");
    }
}
