package com.example.bookingapi.infrastructure.controllers.restaurant.dtos;

import com.example.bookingapi.infrastructure.controllers.response.BaseResponse;

import java.util.List;

public class RestaurantSearchResponse extends BaseResponse {

    private List<RestaurantDTO> restaurantDTOS;

    public void setRestaurants(List<RestaurantDTO> restaurantDTOs) {
        this.restaurantDTOS = restaurantDTOs;
    }

    public List<RestaurantDTO> getRestaurantDTOS() {
        return restaurantDTOS;
    }
}
