package com.ysf.spring6.rest.mvc.repository;

import com.ysf.spring6.rest.mvc.bootstrap.BootstrapTestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(BootstrapTestData.class)
class BeerRepositoryTest {

    @Autowired
    private BeerRepository beerRepository;

    @Test
    @DisplayName("Test beer data populated")
    void testBeerDataPopulated() {
        final int TEST_BEER_DATA_COUNT = 2413;
        Assertions.assertEquals(TEST_BEER_DATA_COUNT, this.beerRepository.count());
    }
}