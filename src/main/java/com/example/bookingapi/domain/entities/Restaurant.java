package com.example.bookingapi.domain.entities;

import java.util.Set;

public class Restaurant {

    private String id;
    private String name;
    private Set<DietaryRestriction> dietaryRestrictions;

    public Restaurant(String id, String name, Set<DietaryRestriction> dietaryRestrictions) {
        this.id = id;
        this.name = name;
        this.dietaryRestrictions = dietaryRestrictions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<DietaryRestriction> getDietaryRestrictions() {
        return dietaryRestrictions;
    }
}
