package com.example.bookingapi.infrastructure.repositories.restaurant;

import com.example.bookingapi.domain.entities.Table;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TableRowMapper  implements RowMapper<Table> {

    @Override
    public Table mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Table(
                rs.getInt("TableId"),
                rs.getInt("RestaurantId"),
                rs.getInt("Capacity")
        );
    }
}
