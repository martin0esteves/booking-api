package com.example.bookingapi.domain.usecases;

import com.example.bookingapi.domain.entities.Reservation;
import com.example.bookingapi.domain.exceptions.ReservationNotFoundException;
import com.example.bookingapi.domain.exceptions.UnAuthorizedUserException;
import com.example.bookingapi.ports.driven.IRestaurantRepository;
import com.example.bookingapi.ports.driven.IUserRepository;
import com.example.bookingapi.ports.driven.IReservationRepository;
import com.example.bookingapi.ports.driver.IDeleteReservation;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DeleteReservation implements IDeleteReservation {

    private final IReservationRepository reservationRepository;

    public DeleteReservation(IRestaurantRepository restaurantRepository,
                             IUserRepository userRepository,
                             IReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void delete(int reservationId, int userId)
            throws ReservationNotFoundException, UnAuthorizedUserException {

        // Authorization to prevent [A01:2021 – Broken Access Control]
        List<Reservation> reservations = reservationRepository.get(reservationId);
        if (reservations.isEmpty()) {
            throw new ReservationNotFoundException();
        }
        Reservation reservation = reservations.getFirst();
        if (!reservation.getEaterIds().contains(userId)) {
            throw new UnAuthorizedUserException();
        }

        reservationRepository.delete(reservationId);
    }
}
