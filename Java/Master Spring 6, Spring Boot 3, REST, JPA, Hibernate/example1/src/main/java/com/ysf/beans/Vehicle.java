package com.ysf.beans;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class Vehicle {
    private String name;

    public Vehicle() {}

    public Vehicle(String name) {
        this.name = name;
    }

    @PostConstruct
    public void init() {
        if (this.name == null) {
            this.name = "Honda";
        }
    }

    @PreDestroy
    public void beforeDestroy() {
        System.out.println("Pre destroy called.");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
