package com.example.bookingapi.domain.entities;

import java.util.Collection;
import java.util.List;

public class User {

    private Integer id;
    private String name;
    private List<DietaryRestriction> dietaryRestrictions;

    public User(Integer id, String name, List<DietaryRestriction> dietaryRestrictions) {
        this.id = id;
        this.name = name;
        this.dietaryRestrictions = dietaryRestrictions;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDietaryRestrictions(List<DietaryRestriction> dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Collection<DietaryRestriction> getDietaryRestrictions() {
        return dietaryRestrictions;
    }
}
