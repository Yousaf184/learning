package com.ysf.spring6.reactive.repository;

import com.ysf.spring6.reactive.model.Person;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class PersonRepositoryImplTest {
    private final IPersonRepository personRepository = new PersonRepositoryImpl();

    @Test
    void testMonoByIdBlock() {
        Mono<Person> personMono = this.personRepository.getById(3);
        Person person = personMono.block();
        System.out.println(person);
    }

    @Test
    void testGetByIdSubscriber() {
        Mono<Person> personMono = this.personRepository.getById(1);
        personMono.subscribe(System.out::println);
    }

    @Test
    void testMapOperation() {
        Mono<Person> personMono = this.personRepository.getById(1);
        personMono.map(Person::getFirstName).subscribe(System.out::println);
    }

    @Test
    void testFluxBlock() {
        Flux<Person> personFlux = this.personRepository.getAll();
        Person person = personFlux.blockFirst();
        System.out.println(person);
    }

    @Test
    void testFluxSubscribe() {
        Flux<Person> personFlux = this.personRepository.getAll();
        personFlux.subscribe(System.out::println);
    }

    @Test
    void testFluxFilter() {
        Flux<Person> personFlux = this.personRepository.getAll();
        personFlux.filter(person -> person.getFirstName().equals("Fiona"))
                .subscribe(System.out::println);
    }

    @Test
    void testFluxToMono() {
        Flux<Person> personFlux = this.personRepository.getAll();
        Mono<Person> personMono = personFlux.next();
        personMono.subscribe(System.out::println);
    }

    @Test
    void testNotFoundId() {
        Flux<Person> personFlux = this.personRepository.getAll();
        Mono<Person> personMono = personFlux
                .filter(person -> person.getId() == -1)
                .single()
                .doOnError(throwable -> {
                    System.out.println("Error in Flux - ID not found");
                    System.out.println(throwable.getMessage());
                });
        personMono.subscribe(System.out::println, (throwable ->  {
            System.out.println("Error in Mono - ID not found");
            System.out.println(throwable.getMessage());
        }));
    }

    @Test
    void testStepVerifierIdFound() {
        Mono<Person> personMono = this.personRepository.getById(1);
        StepVerifier.create(personMono).expectNextCount(1).verifyComplete();
    }

    @Test
    void testStepVerifierIdNotFound() {
        Mono<Person> personMono = this.personRepository.getById(20);
        StepVerifier.create(personMono).expectNextCount(0).verifyComplete();
    }
}