package com.example.bookingapi.infrastructure.repositories.reservation;

import com.example.bookingapi.domain.entities.Reservation;
import com.example.bookingapi.ports.driven.IReservationRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ReservationRepository implements IReservationRepository {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final JdbcTemplate jdbcTemplate;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Reservation> get(int reservationId) {
        String sql =
                "SELECT t.RestaurantId, reserv.TableId, reserv.StartingTime, reserv.EndingTime, re.EaterId " +
                        "FROM Reservation reserv " +
                        "INNER JOIN Tables t ON reserv.TableId = t.TableId " +
                        "INNER JOIN ReservationEater re ON reserv.ReservationId = re.ReservationId " +
                        "WHERE reserv.ReservationId = ?";
        List<Map<String, Object>> reservationEater = jdbcTemplate.queryForList(sql, reservationId);

        if (reservationEater.isEmpty()) {
            return List.of();
        }
        int restaurantId = (int) reservationEater.getFirst().get("RestaurantId");
        int tableId = (int) reservationEater.getFirst().get("TableId");
        LocalDateTime startTime = ((Timestamp)reservationEater.getFirst().get("StartingTime")).toLocalDateTime();
        LocalDateTime endTime = ((Timestamp)reservationEater.getFirst().get("EndingTime")).toLocalDateTime();

        List<Integer> eaters = new ArrayList<>();
        for (Map<String,Object> row : reservationEater) {
            eaters.add((Integer) row.get("EaterId"));
        }

        return List.of(new Reservation(restaurantId, tableId, eaters, startTime, endTime));
    }

    @Override
    public int save(Reservation reservation) {
        // Insert into Reservation table
        SimpleJdbcInsert reservationInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Reservation")
                .usingGeneratedKeyColumns("ReservationId");

        Map<String, Object> reservationParams = new HashMap<>();
        reservationParams.put("TableId", reservation.getTableId());
        reservationParams.put("StartingTime", FORMATTER.format(reservation.getReservationStartTime()));
        reservationParams.put("EndingTime", FORMATTER.format(reservation.getReservationEndTime()));

        int reservationId = reservationInsert.executeAndReturnKey(reservationParams).intValue();

        // Insert into ReservationEater table using SqlParameterSource
        SimpleJdbcInsert reservationEaterInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("ReservationEater");

        List<SqlParameterSource> batchParams = new ArrayList<>();
        for (Integer eaterId : reservation.getEaterIds()) {
            SqlParameterSource paramSource = new MapSqlParameterSource()
                    .addValue("ReservationId", reservationId)
                    .addValue("EaterId", eaterId);
            batchParams.add(paramSource);
        }

        reservationEaterInsert.executeBatch(batchParams.toArray(SqlParameterSource[]::new));
        return reservationId;
    }

    @Override
    public void delete(int reservationId) {
        // Delete from ReservationEater
        String deleteReservationEaterSql = "DELETE FROM ReservationEater WHERE ReservationId = ?";
        jdbcTemplate.update(deleteReservationEaterSql, reservationId);

        // Delete from Reservation
        String deleteReservationSql = "DELETE FROM Reservation WHERE ReservationId = ?";
        jdbcTemplate.update(deleteReservationSql, reservationId);
    }
}
