package com.example.bookingapi.infrastructure.controllers.reservation.dto;

import java.util.List;

public class ReservationRequest {

    private String restaurantId;
    private List<String> eaters;
    private String time;

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public List<String> getEaters() {
        return eaters;
    }

    public void setEaters(List<String> eaters) {
        this.eaters = eaters;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
