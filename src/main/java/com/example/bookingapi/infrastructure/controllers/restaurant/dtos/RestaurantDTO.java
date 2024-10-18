package com.example.bookingapi.infrastructure.controllers.restaurant.dtos;

public class RestaurantDTO {
    private String id;
    private String name;

    public RestaurantDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}