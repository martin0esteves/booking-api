package com.example.bookingapi.infrastructure.repositories.user;

import com.example.bookingapi.domain.entities.DietaryRestriction;
import com.example.bookingapi.domain.entities.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNumber) throws SQLException {
        List<DietaryRestriction> dietaryRestrictions = new ArrayList<>();
        if (rs.getBoolean("Vegetarian")) {
            dietaryRestrictions.add(DietaryRestriction.VEGETARIAN);
        }
        if (rs.getBoolean("Vegan")) {
            dietaryRestrictions.add(DietaryRestriction.VEGAN);
        }
        return new User(rs.getInt("EaterId"), rs.getString("Name"), dietaryRestrictions);
    }
}
