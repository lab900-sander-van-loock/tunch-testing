package com.lab900.tunch.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class BeerBottleTest {

    @ParameterizedTest
    @ValueSource(strings = {"2022-12-01", "2023-01-01", "2023-01-02"})
    void isExpired(String date) {
        BeerBottle beerBottle = new BeerBottle();
        beerBottle.setExpirationDate(LocalDate.parse(date));
        assertTrue(beerBottle.isExpired());
    }

    @ParameterizedTest
    @ValueSource(strings = {"2023-12-01", "2023-05-01", "2023-07-02"})
    void isNotExpired(String date) {
        BeerBottle beerBottle = new BeerBottle();
        beerBottle.setExpirationDate(LocalDate.parse(date));
        assertFalse(beerBottle.isExpired());
    }

    @ParameterizedTest
    @ValueSource(strings = {"2022-12-02", "2023-01-01", "2023-01-02"})
    void isExpiredOn(String date) {
        BeerBottle beerBottle = new BeerBottle();
        beerBottle.setExpirationDate(LocalDate.parse("2022-12-01"));
        assertTrue(beerBottle.isExpiredOn(LocalDate.parse(date)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2023-10-01", "2023-05-01", "2023-07-02"})
    void isNotExpiredOn(String date) {
        BeerBottle beerBottle = new BeerBottle();
        beerBottle.setExpirationDate(LocalDate.parse("2023-11-01"));
        assertFalse(beerBottle.isExpiredOn(LocalDate.parse(date)));
    }


}
