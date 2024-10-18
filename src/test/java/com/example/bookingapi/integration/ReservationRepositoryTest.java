package com.example.bookingapi.integration;

import com.example.bookingapi.domain.entities.Reservation;
import com.example.bookingapi.ports.driven.IReservationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ReservationRepositoryTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private IReservationRepository reservationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void getReservation() {
        List<Reservation> reservations = reservationRepository.get(1);
        assertEquals(1, reservations.size());
        assertEquals(1, reservations.getFirst().getRestaurantId());
        assertEquals(1, reservations.getFirst().getTableId());
        assertEquals(2, reservations.getFirst().getEaterIds().size());
        assertEquals("2024-09-25 18:00:00", reservations.getFirst().getReservationStartTime()
                .truncatedTo(ChronoUnit.HOURS).format(FORMATTER));
        assertEquals("2024-09-25 20:00:00", reservations.getFirst().getReservationEndTime()
                .truncatedTo(ChronoUnit.HOURS).format(FORMATTER));
    }

    @Test
    public void whenInsertCorrectReservationItIsSaved() {
        // Create sample data
        int restaurantId = 1;
        int tableId = 1;
        List<Integer> eaterIds = List.of(1, 2);
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(2);

        // Call the reserve method
        reservationRepository.save(new Reservation(restaurantId, tableId, eaterIds, startTime, endTime));

        // Verify the reservation in the Reservation table
        String selectReservationSql = "SELECT * FROM Reservation WHERE TableId = ? AND StartingTime = ? AND EndingTime = ?";
        List<Map<String, Object>> reservationResults = jdbcTemplate.queryForList(selectReservationSql, tableId,
                FORMATTER.format(startTime), FORMATTER.format(endTime));
        assertEquals(1, reservationResults.size());
        Map<String, Object> reservation = reservationResults.get(0);
        assertEquals(tableId, reservation.get("TableId"));
        assertEquals(FORMATTER.format(startTime),
                ((Timestamp)reservation.get("StartingTime")).toLocalDateTime().truncatedTo(ChronoUnit.SECONDS).format(FORMATTER));
        assertEquals(FORMATTER.format(endTime),
                ((Timestamp)reservation.get("EndingTime")).toLocalDateTime().truncatedTo(ChronoUnit.SECONDS).format(FORMATTER));

        // Verify the eater associations in the ReservationEater table
        String selectReservationEaterSql = "SELECT * FROM ReservationEater WHERE ReservationId = ?";
        List<Map<String, Object>> reservationEaterResults = jdbcTemplate.queryForList(selectReservationEaterSql, reservation.get("ReservationId"));
        assertEquals(eaterIds.size(), reservationEaterResults.size());
        for (Map<String, Object> reservationEater : reservationEaterResults) {
            assertTrue(eaterIds.contains(reservationEater.get("EaterId")));
        }
    }

    @Test
    void whenInsertReservationWithNonExistingTableThenRollbacks() {

    }

    @Test
    void whenInsertReservationWithNonUserThenRollbacks() {

    }

    @Test
    public void testDeleteReservation() throws Exception {
        // Create a reservation
        LocalDateTime start = LocalDateTime.of(2024, 9, 26, 12, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 26, 13, 59);
        Reservation reservation = new Reservation(1, 1, List.of(1), start, end);
        int reservationId = reservationRepository.save(reservation);

        // Delete the reservation
        reservationRepository.delete(reservationId);

        // Verify that the reservation is deleted
        String selectReservationSql = "SELECT * FROM Reservation WHERE ReservationId = ?";
        List<Map<String, Object>> reservationResults = jdbcTemplate.queryForList(selectReservationSql, reservationId);
        assertEquals(0, reservationResults.size());

        // Verify that the associated ReservationEater records are deleted
        String selectReservationEaterSql = "SELECT * FROM ReservationEater WHERE ReservationId = ?";
        List<Map<String, Object>> reservationEaterResults = jdbcTemplate.queryForList(selectReservationEaterSql, reservationId);
        assertEquals(0, reservationEaterResults.size());
    }
}
