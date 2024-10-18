package com.example.bookingapi.ports.driven;

import com.example.bookingapi.domain.entities.Reservation;
import com.example.bookingapi.domain.entities.User;

import java.util.List;

public interface IUserRepository {
    List<User> getUsers(List<Integer> eatersIds);
    List<Reservation> getReservations(int userId);
}
