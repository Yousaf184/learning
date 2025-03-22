package com.ysf.spring6.rest.mvc.repository;

import com.ysf.spring6.rest.mvc.entity.Beer;
import com.ysf.spring6.rest.mvc.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BeerRepository beerRepository;

    private Beer testBeer;

    @BeforeEach
    void setUp() {
        this.testBeer = beerRepository.findAll().getFirst();
    }

    @Transactional
    @Test
    void testAddCategory() {
        Category category = Category.builder()
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .description("Ales")
                .build();

        Category savedCategory = categoryRepository.save(category);

        this.testBeer.addCategory(savedCategory);
        Beer saveBeer = beerRepository.save(testBeer);

        System.out.println(saveBeer.getBeerName());
        System.out.println(saveBeer.getCategories().size());
    }
}