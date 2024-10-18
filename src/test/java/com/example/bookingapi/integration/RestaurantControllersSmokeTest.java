package com.example.bookingapi.integration;

import com.example.bookingapi.infrastructure.controllers.response.ErrorResponse;
import com.example.bookingapi.infrastructure.controllers.restaurant.dtos.RestaurantSearchRequest;
import com.example.bookingapi.infrastructure.controllers.restaurant.dtos.RestaurantSearchResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class RestaurantControllersSmokeTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void smokeTestAvailability() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Create a request object
        String url = "http://localhost:" + port + "/v1/restaurant/search";
        RestaurantSearchRequest request = new RestaurantSearchRequest();
        request.setSearchType("Availability");
        request.setEatersIds(Arrays.asList("1", "2", "3"));
        request.setStartTime(LocalDateTime.parse("2024-09-25 22:30:00", formatter));

        // Perform the POST request
        ResponseEntity<RestaurantSearchResponse> response = restTemplate.
                postForEntity(url, request, RestaurantSearchResponse.class);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getRestaurantDTOS());
        assertEquals(1, response.getBody().getRestaurantDTOS().size());
        assertEquals("Restaurant A", response.getBody().getRestaurantDTOS().getFirst().getName());
    }

    @Test
    public void testSearchRestaurants_InvalidSearchType() throws Exception {
        // Create a request with an invalid search type
        String url = "http://localhost:" + port + "/v1/restaurant/search";
        RestaurantSearchRequest request = new RestaurantSearchRequest();
        request.setSearchType("InvalidSearchType");
        request.setEatersIds(Arrays.asList("1", "2", "3"));
        request.setStartTime(LocalDateTime.parse("2024-10-17 19:30:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // Perform the POST request
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(url, request, ErrorResponse.class);

        // Assert the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid search type", response.getBody().getResponseMessage());
    }

    @Test
    public void testSearchRestaurants_MissingProperty() throws Exception {
        // Create a request with an invalid search type
        String url = "http://localhost:" + port + "/v1/restaurant/search";
        RestaurantSearchRequest request = new RestaurantSearchRequest();
        // request.setSearchType("InvalidSearchType"); <------ not sending this
        request.setEatersIds(Arrays.asList("1", "2", "3"));
        request.setStartTime(LocalDateTime.parse("2024-10-17 19:30:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        // Perform the POST request
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(url, request, ErrorResponse.class);

        // Assert the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid search type", response.getBody().getResponseMessage());
    }
}