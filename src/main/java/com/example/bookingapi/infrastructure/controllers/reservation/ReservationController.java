package com.example.bookingapi.infrastructure.controllers.reservation;

import com.example.bookingapi.domain.exceptions.*;
import com.example.bookingapi.infrastructure.controllers.reservation.dto.ReservationRequest;
import com.example.bookingapi.infrastructure.controllers.reservation.dto.ReservationResponse;
import com.example.bookingapi.infrastructure.controllers.response.BaseResponse;
import com.example.bookingapi.infrastructure.controllers.response.ErrorResponse;
import com.example.bookingapi.ports.driver.IDeleteReservation;
import com.example.bookingapi.ports.driver.IReserveRestaurantTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class ReservationController {

    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final static Logger logger = LoggerFactory.getLogger(ReservationController.class);
    private final IReserveRestaurantTable reserveRestaurantTable;
    private final IDeleteReservation deleteReservation;

    @Autowired
    public ReservationController(IReserveRestaurantTable reserveRestaurantTable, IDeleteReservation deleteReservation) {
        this.reserveRestaurantTable = reserveRestaurantTable;
        this.deleteReservation = deleteReservation;
    }

    @PostMapping("v1/reservation")
    public ResponseEntity<BaseResponse> createReservation(@RequestBody ReservationRequest request) {
        try {
            LocalDateTime reservationStart = LocalDateTime.parse(request.getTime(), FORMATTER);
            int reservationID = reserveRestaurantTable.reserve(Integer.parseInt(request.getRestaurantId()),
                    request.getEaters().stream().map(Integer::parseInt).toList(), reservationStart);
            return new ResponseEntity<>(new ReservationResponse(reservationID), HttpStatus.OK);

        } catch (UsersNotFoundException | DietaryRestrictionsException | TableNotAvailableException
                 | RestaurantNotFoundException | ReservationOverlapException e) {
            HttpStatus badRequest = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(new ErrorResponse(badRequest.value(), e.getClass().getSimpleName()), badRequest);
        } catch (RuntimeException ex) {
            logger.error("Unhandled error while creating reservation", ex);
            HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(new ErrorResponse(internalServerError.value(), ""), internalServerError);
        }
    }

    @DeleteMapping("v1/reservation/{reservationId}")
    public ResponseEntity<BaseResponse> deleteReservation(@RequestHeader("userId") String userId,
                                                          @PathVariable String reservationId) {
        try {
            int reservationIdInt = Integer.parseInt(reservationId);
            int userIdInt = Integer.parseInt(userId);
            deleteReservation.delete(reservationIdInt, userIdInt);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (UsersNotFoundException | ReservationNotFoundException e) {
            HttpStatus badRequest = HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(new ErrorResponse(badRequest.value(), e.getClass().getSimpleName()), badRequest);
        } catch (UnAuthorizedUserException e) {
            HttpStatus badRequest = HttpStatus.UNAUTHORIZED;
            return new ResponseEntity<>(new ErrorResponse(badRequest.value(), e.getClass().getSimpleName()), badRequest);
        } catch (RuntimeException ex) {
            logger.error("Unhandled error while creating reservation", ex);
            HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(new ErrorResponse(internalServerError.value(), internalServerError.getReasonPhrase()), internalServerError);
        }
    }

}
