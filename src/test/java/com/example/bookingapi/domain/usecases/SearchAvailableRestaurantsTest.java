package com.example.bookingapi.domain.usecases;

import com.example.bookingapi.domain.entities.DietaryRestriction;
import com.example.bookingapi.domain.entities.Restaurant;
import com.example.bookingapi.domain.entities.User;
import com.example.bookingapi.domain.exceptions.ReservationOverlapException;
import com.example.bookingapi.domain.exceptions.UsersNotFoundException;
import com.example.bookingapi.ports.driven.IRestaurantRepository;
import com.example.bookingapi.ports.driven.IUserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class SearchAvailableRestaurantsTest {

    @Test
    void whenSomeUsersAreNotFoundThenException() {
        IUserRepository userRepository = Mockito.mock(IUserRepository.class);
        when(userRepository.getUsers(any())).thenReturn(Collections.emptyList());
        SearchAvailableRestaurants searchAvailableRestaurants = new SearchAvailableRestaurants(null, userRepository);
        UsersNotFoundException usersNotFoundException = assertThrows(
                UsersNotFoundException.class,
                () -> searchAvailableRestaurants.findAvailableRestaurants(
                    List.of(1, 2),
                    LocalDateTime.of(2024, 9, 25, 18, 0))
            );
    }
}