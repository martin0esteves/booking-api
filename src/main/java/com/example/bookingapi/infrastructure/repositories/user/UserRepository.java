package com.example.bookingapi.infrastructure.repositories.user;

import com.example.bookingapi.domain.entities.Reservation;
import com.example.bookingapi.domain.entities.User;
import com.example.bookingapi.infrastructure.repositories.reservation.ReservationRepository;
import com.example.bookingapi.ports.driven.IUserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class UserRepository implements IUserRepository {

    private final ReservationRepository reservationRepository;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(ReservationRepository reservationRepository, NamedParameterJdbcTemplate namedJdbcTemplate,
                          JdbcTemplate jdbcTemplate) {
        this.reservationRepository = reservationRepository;
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers(List<Integer> eaterIds) {
        String sql = "SELECT * FROM Eater WHERE EaterId IN (:ids)";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", eaterIds.stream().toList());
        return namedJdbcTemplate.query(sql, parameters, new UserRowMapper());
    }

    @Override
    public List<Reservation> getReservations(int userId) {
        String sql = "SELECT re.ReservationId FROM ReservationEater re WHERE re.EaterId = ?";
        List<Map<String, Object>> reservationEater = jdbcTemplate.queryForList(sql, userId);
        List<Reservation> reservations = new ArrayList<>();
        reservationEater.forEach(re -> {
            Reservation reservation = reservationRepository.get((Integer) re.get("ReservationId")).getFirst();
            reservations.add(reservation);
        });
        return reservations;
    }
}