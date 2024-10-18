package com.example.bookingapi.infrastructure.controllers.restaurant.dtos;

import java.time.LocalDateTime;
import java.util.List;

public class RestaurantSearchRequest {

    private String searchType;
    private List<String> eatersIds;
    private LocalDateTime startTime;

    public String getSearchType() {
        return searchType;
    }

    public List<String> getEatersIds() {
        return eatersIds;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public void setEatersIds(List<String> eatersIds) {
        this.eatersIds = eatersIds;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}