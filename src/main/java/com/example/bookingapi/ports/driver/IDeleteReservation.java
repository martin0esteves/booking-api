package com.example.bookingapi.ports.driver;

import com.example.bookingapi.domain.exceptions.ReservationNotFoundException;
import com.example.bookingapi.domain.exceptions.UnAuthorizedUserException;
import com.example.bookingapi.domain.exceptions.UsersNotFoundException;

public interface IDeleteReservation {
    void delete(int reservationId, int userId) throws ReservationNotFoundException, UsersNotFoundException, UnAuthorizedUserException;
}
