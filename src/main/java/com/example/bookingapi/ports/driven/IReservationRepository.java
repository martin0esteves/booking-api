package com.example.bookingapi.ports.driven;

import com.example.bookingapi.domain.entities.Reservation;

import java.util.List;

public interface IReservationRepository {
    List<Reservation> get(int reservation);
    int save(Reservation reservation);
    void delete(int reservationId);
}
