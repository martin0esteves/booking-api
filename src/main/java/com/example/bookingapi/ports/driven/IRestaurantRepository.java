package com.example.bookingapi.ports.driven;

import com.example.bookingapi.domain.entities.DietaryRestriction;
import com.example.bookingapi.domain.entities.Restaurant;
import com.example.bookingapi.domain.entities.Table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface IRestaurantRepository {

    List<Restaurant> getRestaurant(int restaurantId);
    List<Table> getAvailableTables(int restaurantId, LocalDateTime reservationStartTime, LocalDateTime reservationEndTime, int size);
    List<Restaurant> findRestaurantsWithAvailableTables(int tableMinCapacity, LocalDateTime start, LocalDateTime end, Set<DietaryRestriction> dietaryRestrictions);
}
