package com.example.bookingapi.domain.entities;

import java.time.LocalDateTime;
import java.util.List;

public class Reservation {


    private final int restaurantId;
    private final int tableId;
    private final List<Integer> eaterIds;
    private final LocalDateTime reservationStartTime;
    private final LocalDateTime reservationEndTime;

    public Reservation(int restaurantId, int tableId, List<Integer> eaterIds, LocalDateTime reservationStartTime,
                       LocalDateTime reservationEndTime) {
        this.restaurantId = restaurantId;
        this.tableId = tableId;
        this.eaterIds = eaterIds;
        this.reservationStartTime = reservationStartTime;
        this.reservationEndTime = reservationEndTime;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public int getTableId() {
        return tableId;
    }

    public List<Integer> getEaterIds() {
        return eaterIds;
    }

    public LocalDateTime getReservationStartTime() {
        return reservationStartTime;
    }

    public LocalDateTime getReservationEndTime() {
        return reservationEndTime;
    }
}