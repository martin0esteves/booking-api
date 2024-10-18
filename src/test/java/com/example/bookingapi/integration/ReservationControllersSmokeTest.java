package com.example.bookingapi.integration;

import com.example.bookingapi.infrastructure.controllers.reservation.dto.ReservationRequest;
import com.example.bookingapi.infrastructure.controllers.reservation.dto.ReservationResponse;
import com.example.bookingapi.infrastructure.controllers.response.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReservationControllersSmokeTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateReservation() throws Exception {
        // Create a request object
        String url = "http://localhost:" + port + "/v1/reservation";
        ReservationRequest request = new ReservationRequest();
        request.setRestaurantId("1");
        request.setEaters(Arrays.asList("1", "2", "3"));
        request.setTime("2024-10-17 19:30:00");

        // Perform the POST request
        ResponseEntity<ReservationResponse> response = restTemplate.postForEntity(url, request, ReservationResponse.class);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getReservationId());
    }

    @Test
    public void testCreateReservation_InvalidRestaurantId() throws Exception {
        // Create a request with an invalid restaurant ID
        String url = "http://localhost:" + port + "/v1/reservation";
        ReservationRequest request = new ReservationRequest();
        request.setRestaurantId("0"); // Invalid restaurant ID
        request.setEaters(Arrays.asList("1", "2", "3"));
        request.setTime("2024-10-17 19:30:00");

        // Perform the POST request
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(url, request, ErrorResponse.class);

        // Assert the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getResponseCode());
        assertEquals("RestaurantNotFoundException", response.getBody().getResponseMessage());
    }
}