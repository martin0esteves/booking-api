package com.example.bookingapi.infrastructure.repositories.restaurant;

import com.example.bookingapi.domain.entities.DietaryRestriction;
import com.example.bookingapi.domain.entities.Restaurant;
import com.example.bookingapi.ports.driven.IRestaurantRepository;
import com.example.bookingapi.domain.entities.Table;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Component
public class RestaurantRepository implements IRestaurantRepository {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final JdbcTemplate jdbcTemplate;

    public RestaurantRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Restaurant> getRestaurant(int restaurantId) {
        String sql =
                "SELECT r.RestaurantId, r.Name, r.Vegetarian, r.Vegan " +
                "FROM Restaurants r " +
                "WHERE RestaurantId = ?";
        return jdbcTemplate.query(
                sql,
                new RestaurantRowMapper(),
                restaurantId
        );
    }

    @Override
    public List<Table> getAvailableTables(int restaurantId, LocalDateTime reservationStartTime,
                                          LocalDateTime reservationEndTime, int minCapacity) {
        String sql =
                "SELECT t.TableId, t.RestaurantId, t.Capacity " +
                "FROM Tables t " +
                "WHERE t.RestaurantId = ? " +
                    "AND t.Capacity >= ? " +
                    "AND NOT EXISTS (SELECT 1 " +
                        "FROM Reservation re " +
                        "WHERE t.TableId = re.TableId " +
                            "AND (re.StartingTime BETWEEN ? AND ?) " +
                            "OR (re.EndingTime BETWEEN ? AND ?) " +
                            "OR (? BETWEEN re.StartingTime AND re.EndingTime)" +
                        ") ";
        return jdbcTemplate.query(
                sql,
                new TableRowMapper(),
                restaurantId,
                minCapacity,
                reservationStartTime.format(FORMATTER),
                reservationEndTime.format(FORMATTER),
                reservationStartTime.format(FORMATTER),
                reservationEndTime.format(FORMATTER),
                reservationStartTime.format(FORMATTER)
        );
    }

    @Override
    public List<Restaurant> findRestaurantsWithAvailableTables(
            int tableMinCapacity, LocalDateTime start, LocalDateTime end, Set<DietaryRestriction> dietaryRestrictions) {

        String sql =
                "SELECT DISTINCT r.RestaurantId, r.Name, r.Vegetarian, r.Vegan " +
                "FROM Restaurants r " +
                "JOIN Tables t ON r.RestaurantId = t.RestaurantId " +
                "WHERE t.Capacity >= ? {DIETARY_WHERE}" +
                "  AND NOT EXISTS ( " +
                "    SELECT 1 " +
                "    FROM Reservation re " +
                "    WHERE t.TableId = re.TableId " +
                "      AND (re.StartingTime BETWEEN ? AND ? " +
                "      OR re.EndingTime BETWEEN ? AND ? " +
                "      OR ? BETWEEN re.StartingTime AND re.EndingTime) " +
                "  );";

        // add check for dietary restrictions
        if (dietaryRestrictions.isEmpty()) {
            sql = sql.replace("{DIETARY_WHERE}", "");
        } else {
            StringBuilder dietaryWhere = new StringBuilder();
            for (DietaryRestriction dietaryRestriction : dietaryRestrictions) {
              switch (dietaryRestriction) {
                  case VEGETARIAN -> dietaryWhere.append("AND r.Vegetarian = TRUE ");
                  case VEGAN -> dietaryWhere.append("AND r.Vegan = TRUE ");
                  case GLUTEN -> dietaryWhere.append("AND r.Gluten = TRUE ");
                  case PALEO -> dietaryWhere.append("AND r.Paleo = TRUE ");
                  default -> throw new IllegalArgumentException();
              }
            }
            sql = sql.replace("{DIETARY_WHERE}", dietaryWhere.toString());
        }

        return jdbcTemplate.query(
                sql,
                new RestaurantRowMapper(),
                tableMinCapacity,
                start.format(FORMATTER),
                end.format(FORMATTER),
                start.format(FORMATTER),
                end.format(FORMATTER),
                start.format(FORMATTER)
        );
    }
}
