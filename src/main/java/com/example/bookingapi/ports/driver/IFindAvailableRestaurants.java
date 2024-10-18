package com.example.bookingapi.ports.driver;

import com.example.bookingapi.domain.exceptions.ReservationOverlapException;
import com.example.bookingapi.domain.exceptions.UsersNotFoundException;
import com.example.bookingapi.domain.entities.Restaurant;

import java.time.LocalDateTime;
import java.util.List;

public interface IFindAvailableRestaurants {

    List<Restaurant> findAvailableRestaurants(List<Integer> eatersIds, LocalDateTime startTime) throws UsersNotFoundException, ReservationOverlapException;
}
