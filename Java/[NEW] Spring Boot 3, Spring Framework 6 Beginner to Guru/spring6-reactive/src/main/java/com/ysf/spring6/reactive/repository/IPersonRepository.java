package com.ysf.spring6.reactive.repository;

import com.ysf.spring6.reactive.model.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IPersonRepository {

    Mono<Person> getById(Integer id);

    Flux<Person> getAll();
}
