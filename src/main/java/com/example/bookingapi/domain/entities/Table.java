package com.example.bookingapi.domain.entities;

public class Table {

    private int tableId;
    private int restaurantId;
    private int capacity;

    public Table(int tableId, int restaurantId, int capacity) {
        this.tableId = tableId;
        this.restaurantId = restaurantId;
        this.capacity = capacity;
    }

    public int getTableId() {
        return tableId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public int getCapacity() {
        return capacity;
    }
}
