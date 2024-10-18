package com.example.bookingapi.integration;

import com.example.bookingapi.domain.entities.Restaurant;
import com.example.bookingapi.domain.entities.Table;
import com.example.bookingapi.ports.driven.IRestaurantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class RestaurantRepositoryTest {

    @Autowired
    private IRestaurantRepository restaurantRepository;

    @Test
    void whenSearchByIdThenCorrectRestaurantIsReturned() {
        List<Restaurant> restaurants = restaurantRepository.getRestaurant(1);
        assertEquals(1, restaurants.size());
        assertEquals("Restaurant A", restaurants.getFirst().getName());
    }

    @Test
    void whenAllAvailableTablesThenReturnAllAvailableTables() {
        int restaurant = 1;
        int eaters = 2;
        LocalDateTime start = LocalDateTime.of(2024, 9, 25, 12, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 25, 13, 59);

        List<Table> tables = restaurantRepository.getAvailableTables(restaurant, start, end, eaters);
        assertEquals(2, tables.size());
        tables.forEach(t -> assertEquals(1, t.getRestaurantId()));
    }

    // TODO More tests over getAvailableTables

    @Test
    void whenAllAvailableThenReturnAllRestaurants() {
        int eaters = 2;
        LocalDateTime start = LocalDateTime.of(2024, 9, 25, 12, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 25, 13, 59);

        List<Restaurant> restaurants = restaurantRepository.findRestaurantsWithAvailableTables(eaters, start, end, Collections.emptySet());
        assertEquals(3, restaurants.size());
    }

    @Test
    void whenOnlyOneAvailableBecauseEatersQuantityThenReturnOnlyOneRestaurant() {
        int eaters = 6;
        LocalDateTime start = LocalDateTime.of(2024, 9, 25, 12, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 25, 13, 59);

        List<Restaurant> restaurants = restaurantRepository.findRestaurantsWithAvailableTables(eaters, start, end, Collections.emptySet());
        assertEquals(1, restaurants.size());
    }

    @Test
    void whenOnlyOneAvailableBecauseReservationsThenReturnOnlyOneRestaurant() {
        int eaters = 1;
        LocalDateTime start = LocalDateTime.of(2024, 9, 25, 18, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 25, 20, 59);

        List<Restaurant> restaurants = restaurantRepository.findRestaurantsWithAvailableTables(eaters,  start, end, Collections.emptySet());
        assertEquals(1, restaurants.size());
    }


    @Test
    void whenStartAndEndTimeContainsReservationsThenNoAvailability() {
        int eaters = 6;
        LocalDateTime start = LocalDateTime.of(2024, 9, 25, 18, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 25, 23, 0);

        List<Restaurant> restaurants = restaurantRepository.findRestaurantsWithAvailableTables(eaters,  start, end, Collections.emptySet());
        assertEquals(0, restaurants.size());
    }

    @Test
    void whenStartAndEndTimeIsContainedByAnotherReservationThenNoAvailability() {
        int eaters = 6;
        LocalDateTime start = LocalDateTime.of(2024, 9, 25, 19, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 25, 20, 0);

        List<Restaurant> restaurants = restaurantRepository.findRestaurantsWithAvailableTables(eaters,  start, end, Collections.emptySet());
        assertEquals(0, restaurants.size());
    }

    @Test
    void whenEndTimeAtReservationStartTimeThenNoAvailability() {
        int eaters = 6;
        LocalDateTime start = LocalDateTime.of(2024, 9, 25, 17, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 25, 18, 30);

        List<Restaurant> restaurants = restaurantRepository.findRestaurantsWithAvailableTables(eaters,  start, end, Collections.emptySet());
        assertEquals(0, restaurants.size());
    }

    @Test
    void whenEndTimeOneMinuteBeforeThanReservationStartTimeThenAvailability() {
        int eaters = 6;
        LocalDateTime start = LocalDateTime.of(2024, 9, 25, 17, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 25, 18, 29);

        List<Restaurant> restaurants = restaurantRepository.findRestaurantsWithAvailableTables(eaters,  start, end, Collections.emptySet());
        assertEquals(1, restaurants.size());
    }

    @Test
    void whenStartTimeAtReservationEndTimeThenNoAvailability() {
        int eaters = 6;
        LocalDateTime start = LocalDateTime.of(2024, 9, 25, 21, 30);
        LocalDateTime end = LocalDateTime.of(2024, 9, 25, 23, 0);

        List<Restaurant> restaurants = restaurantRepository.findRestaurantsWithAvailableTables(eaters,  start, end, Collections.emptySet());
        assertEquals(0, restaurants.size());
    }

    @Test
    void whenStartTimeOneMinuteAfterOfReservationEndTimeThenNoAvailability() {
        int eaters = 6;
        LocalDateTime start = LocalDateTime.of(2024, 9, 25, 21, 31);
        LocalDateTime end = LocalDateTime.of(2024, 9, 25, 23, 0);

        List<Restaurant> restaurants = restaurantRepository.findRestaurantsWithAvailableTables(eaters,  start, end, Collections.emptySet());
        assertEquals(1, restaurants.size());
    }
}