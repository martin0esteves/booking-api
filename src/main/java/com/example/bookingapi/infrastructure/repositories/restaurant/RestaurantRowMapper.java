package com.example.bookingapi.infrastructure.repositories.restaurant;

import com.example.bookingapi.domain.entities.DietaryRestriction;
import com.example.bookingapi.domain.entities.Restaurant;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

class RestaurantRowMapper implements RowMapper<Restaurant> {

    @Override
    public Restaurant mapRow(ResultSet rs, int rowNum) throws SQLException {

        Set<DietaryRestriction> dietaryRestrictions = new HashSet<>();
        boolean vegan = rs.getBoolean("Vegan");
        if (vegan) {
            dietaryRestrictions.add(DietaryRestriction.VEGAN);
        }
        boolean vegetarian = rs.getBoolean("Vegetarian");
        if (vegetarian) {
            dietaryRestrictions.add(DietaryRestriction.VEGETARIAN);
        }

        return new Restaurant(
                String.valueOf(rs.getInt("RestaurantId")),
                rs.getString("Name"),
                dietaryRestrictions
        );
    }
}