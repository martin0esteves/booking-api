package com.example.bookingapi.infrastructure.controllers.restaurant;

import com.example.bookingapi.domain.exceptions.ReservationOverlapException;
import com.example.bookingapi.domain.exceptions.UsersNotFoundException;
import com.example.bookingapi.infrastructure.controllers.response.BaseResponse;
import com.example.bookingapi.infrastructure.controllers.response.ErrorResponse;
import com.example.bookingapi.infrastructure.controllers.restaurant.dtos.RestaurantDTO;
import com.example.bookingapi.infrastructure.controllers.restaurant.dtos.RestaurantSearchRequest;
import com.example.bookingapi.infrastructure.controllers.restaurant.dtos.RestaurantSearchResponse;
import com.example.bookingapi.domain.entities.Restaurant;
import com.example.bookingapi.ports.driver.IFindAvailableRestaurants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RestaurantsController {

    private final static Logger logger = LoggerFactory.getLogger(RestaurantsController.class);
    private final IFindAvailableRestaurants findAvailableRestaurants;

    @Autowired
    public RestaurantsController(IFindAvailableRestaurants findAvailableRestaurants) {
        this.findAvailableRestaurants = findAvailableRestaurants;
    }

    @PostMapping("v1/restaurant/search")
    public ResponseEntity<BaseResponse> searchRestaurants(@RequestBody RestaurantSearchRequest request) {
        try {
            if (request.getSearchType().equals("Availability")) {
                return processAvailabilityRequest(request);
            }
            HttpStatus badRequest = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(new ErrorResponse(badRequest.value(), "Invalid search type"), badRequest);

        } catch (UsersNotFoundException | ReservationOverlapException ex) {
            HttpStatus badRequest = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(new ErrorResponse(badRequest.value(), ex.getClass().getSimpleName()), badRequest);
        } catch (RuntimeException e) {
            logger.error("Unhandled exception while searching restaurants", e);
            HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(new ErrorResponse(internalServerError.value(), ""), internalServerError);
        }
    }

    private ResponseEntity<BaseResponse> processAvailabilityRequest(RestaurantSearchRequest request)
            throws UsersNotFoundException, ReservationOverlapException {

            List<Restaurant> restaurants = findAvailableRestaurants
                    .findAvailableRestaurants(request.getEatersIds().stream().map(Integer::parseInt).toList(), request.getStartTime());
            List<RestaurantDTO> restaurantDTOs = new ArrayList<>();
            restaurants.forEach(r -> restaurantDTOs.add(new RestaurantDTO(r.getId(), r.getName())));
            RestaurantSearchResponse response = new RestaurantSearchResponse();
            response.setRestaurants(restaurantDTOs);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
