package com.ysf.spring6.reactive.repository;

import com.ysf.spring6.reactive.model.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class PersonRepositoryImpl implements IPersonRepository {

    private final List<Person> personList;

    public PersonRepositoryImpl() {
        this.personList = List.of(
            Person.builder().id(1).firstName("Michael").lastName("Weston").build(),
            Person.builder().id(2).firstName("Fiona").lastName("Glen").build(),
            Person.builder().id(3).firstName("Sam").lastName("Axe").build(),
            Person.builder().id(4).firstName("Jesse").lastName("Porter").build()
        );
    }

    @Override
    public Mono<Person> getById(Integer id) {
        List<Person> personList = this.personList.stream()
                .filter(p -> p.getId().equals(id))
                .toList();

        if (personList.size() > 1) {
            throw new IllegalArgumentException("Found multiple persons with the same id");
        }

        return personList.isEmpty()
                ? Mono.empty()
                : Mono.just(personList.getFirst());
    }

    @Override
    public Flux<Person> getAll() {
        return Flux.fromIterable(this.personList);
    }
}
