package com.upgrad.FoodOrderingApp.service.beans;

import java.util.UUID;

public class RestaurantDetailsResponseAddressState {

    private UUID id;
    private String stateName;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(final String stateName) {
        this.stateName = stateName;
    }
}
