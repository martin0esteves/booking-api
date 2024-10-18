package com.example.bookingapi.domain.usecases;

import com.example.bookingapi.domain.entities.*;
import com.example.bookingapi.domain.exceptions.*;
import com.example.bookingapi.ports.driven.IRestaurantRepository;
import com.example.bookingapi.ports.driven.IUserRepository;
import com.example.bookingapi.ports.driven.IReservationRepository;
import com.example.bookingapi.ports.driver.IReserveRestaurantTable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class CreateReservation implements IReserveRestaurantTable {

    private final IRestaurantRepository restaurantRepository;
    private final IUserRepository userRepository;
    private final IReservationRepository reservationRepository;

    public CreateReservation(IRestaurantRepository restaurantRepository,
                             IUserRepository userRepository,
                             IReservationRepository reservationRepository) {

        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public int reserve(int restaurantId, List<Integer> eaterIds, LocalDateTime reservationStartTime)
            throws UsersNotFoundException, DietaryRestrictionsException, TableNotAvailableException,
            RestaurantNotFoundException, ReservationOverlapException {

        // check users
        List<User> users = userRepository.getUsers(eaterIds);
        if (users.size() != eaterIds.size()) {
            throw new UsersNotFoundException();
        }

        // check dietary restrictions
        List<Restaurant> restaurants = restaurantRepository.getRestaurant(restaurantId);
        if (restaurants.isEmpty()) {
            throw new RestaurantNotFoundException();
        }
        Restaurant restaurant = restaurants.getFirst();
        Set<DietaryRestriction> dietaryRestrictions = new HashSet<>();
        users.forEach(e -> dietaryRestrictions.addAll(e.getDietaryRestrictions()));
        if (!restaurant.getDietaryRestrictions().containsAll(dietaryRestrictions)) {
            throw new DietaryRestrictionsException();
        }

        return reserveTableTransactional(restaurantId, users, reservationStartTime, reservationStartTime.plusHours(2));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    private int reserveTableTransactional(int restaurantId, List<User> users,
                                          LocalDateTime startTime, LocalDateTime endTime)
            throws TableNotAvailableException, ReservationOverlapException {

        // verify no eater overlapping reservation
        for (User u : users) {
            List<Reservation> reservations = userRepository.getReservations(u.getId());
            for (Reservation r : reservations) {
                boolean overlaps = !(endTime.isBefore(r.getReservationStartTime())
                        || startTime.isAfter(r.getReservationEndTime()));
                if (overlaps) {
                    throw new ReservationOverlapException();
                }
            }
        }

        // check available tables and choose one
        List<Table> restaurantTables = restaurantRepository
                .getAvailableTables(restaurantId, startTime, endTime, users.size());
        if (restaurantTables.isEmpty()) {
            throw new TableNotAvailableException();
        }
        Table optimalTable = restaurantTables.getFirst();
        for (Table table : restaurantTables) {
            if (table.getCapacity() < optimalTable.getCapacity()) {
                optimalTable = table;
            }
        }

        // make the reservation
        Reservation reservation = new Reservation(restaurantId, optimalTable.getTableId(),
                users.stream().map(User::getId).toList(), startTime, endTime);

        return reservationRepository.save(reservation);
    }
}
