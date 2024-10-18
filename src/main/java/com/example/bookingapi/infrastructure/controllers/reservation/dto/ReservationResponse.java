package com.example.bookingapi.infrastructure.controllers.reservation.dto;

import com.example.bookingapi.infrastructure.controllers.response.BaseResponse;

public class ReservationResponse extends BaseResponse {

    private String reservationId;

    public ReservationResponse() {
    }

    public ReservationResponse(int reservationID) {
        this.reservationId = Integer.toString(reservationID);
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getReservationId() {
        return reservationId;
    }

}
