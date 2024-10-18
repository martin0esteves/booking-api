package com.example.bookingapi.integration;

import com.example.bookingapi.domain.entities.DietaryRestriction;
import com.example.bookingapi.domain.entities.User;
import com.example.bookingapi.ports.driven.IUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private IUserRepository userRepository;

    @Test
    void whenQueryByOneIdThenReturnsOneUser() {
        List<User> users = userRepository.getUsers(List.of(1));
        assertEquals(1, users.size());
        assertEquals("Scott", users.getFirst().getName());
        assertTrue(users.getFirst().getDietaryRestrictions().contains(DietaryRestriction.VEGETARIAN));
    }

    @Test
    void whenQueryingByNonExistentIdThenReturnsEmpty() {
        List<User> users = userRepository.getUsers(List.of(999));
        assertEquals(0, users.size());
    }

    @Test
    void whenQueryingOneExistentAnd2NonExistentThenReturnsOne() {
        List<User> users = userRepository.getUsers(List.of(1, 998, 999));
        assertEquals(1, users.size());
    }
}