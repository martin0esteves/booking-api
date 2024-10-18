package com.example.bookingapi.ports.driver;

import com.example.bookingapi.domain.exceptions.*;

import java.time.LocalDateTime;
import java.util.List;

public interface IReserveRestaurantTable {
    int reserve(int restaurantId, List<Integer> eaterIds, LocalDateTime reservationStartTime)
            throws UsersNotFoundException, DietaryRestrictionsException, TableNotAvailableException, RestaurantNotFoundException, ReservationOverlapException;
}
