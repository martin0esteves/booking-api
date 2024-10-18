package com.example.bookingapi.domain.usecases;

import com.example.bookingapi.domain.entities.DietaryRestriction;
import com.example.bookingapi.domain.entities.Reservation;
import com.example.bookingapi.domain.entities.User;
import com.example.bookingapi.domain.exceptions.ReservationOverlapException;
import com.example.bookingapi.domain.exceptions.UsersNotFoundException;
import com.example.bookingapi.ports.driven.IUserRepository;
import com.example.bookingapi.ports.driver.IFindAvailableRestaurants;
import com.example.bookingapi.domain.entities.Restaurant;
import com.example.bookingapi.ports.driven.IRestaurantRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SearchAvailableRestaurants implements IFindAvailableRestaurants {

    private final IRestaurantRepository restaurantRepository;
    private final IUserRepository userRepository;

    public SearchAvailableRestaurants(IRestaurantRepository restaurantRepository, IUserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Restaurant> findAvailableRestaurants(List<Integer> eatersIds, LocalDateTime startTime)
            throws UsersNotFoundException, ReservationOverlapException {

        // verify all eaters exists
        List<User> users = userRepository.getUsers(eatersIds);
        if (users.size() != eatersIds.size()) {
            throw new UsersNotFoundException();
        }

        // verify no eater overlapping reservation
        LocalDateTime endTime = startTime.plusHours(2);
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

        // consider dietary restrictions
        Set<DietaryRestriction> dietaryRestrictions = new HashSet<>();
        users.forEach(e -> dietaryRestrictions.addAll(e.getDietaryRestrictions()));

        return restaurantRepository.findRestaurantsWithAvailableTables(users.size(), startTime, endTime, dietaryRestrictions);
    }
}