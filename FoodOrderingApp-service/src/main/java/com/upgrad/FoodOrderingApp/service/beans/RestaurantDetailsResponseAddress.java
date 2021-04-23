package com.upgrad.FoodOrderingApp.service.beans;

import java.util.UUID;

public class RestaurantDetailsResponseAddress {

    private UUID id;

    private String flatBuildingName;

    private String locality;

    private String city;

    private String pincode;

    private RestaurantDetailsResponseAddressState state;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getFlatBuildingName() {
        return flatBuildingName;
    }

    public void setFlatBuildingName(final String flatBuildingName) {
        this.flatBuildingName = flatBuildingName;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(final String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(final String pincode) {
        this.pincode = pincode;
    }

    public RestaurantDetailsResponseAddressState getState() {
        return state;
    }

    public void setState(final RestaurantDetailsResponseAddressState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "RestaurantDetailsResponseAddress{" +
               "id=" + id +
               ", flatBuildingName='" + flatBuildingName + '\'' +
               ", locality='" + locality + '\'' +
               ", city='" + city + '\'' +
               ", pincode='" + pincode + '\'' +
               ", state=" + state +
               '}';
    }
}
