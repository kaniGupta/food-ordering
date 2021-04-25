package com.upgrad.FoodOrderingApp.service.beans;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class RestaurantDetailsResponse {
    private String id;
    private String restaurantName;
    private String photoURL;
    private BigDecimal customerRating;
    private Integer averagePrice;
    private Integer numberCustomersRated;
    private RestaurantDetailsResponseAddress address;
    private List<CategoryList> categories;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(final String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(final String photoURL) {
        this.photoURL = photoURL;
    }

    public BigDecimal getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(final BigDecimal customerRating) {
        this.customerRating = customerRating;
    }

    public Integer getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(final Integer averagePrice) {
        this.averagePrice = averagePrice;
    }

    public Integer getNumberCustomersRated() {
        return numberCustomersRated;
    }

    public void setNumberCustomersRated(final Integer numberCustomersRated) {
        this.numberCustomersRated = numberCustomersRated;
    }

    public RestaurantDetailsResponseAddress getAddress() {
        return address;
    }

    public void setAddress(final RestaurantDetailsResponseAddress address) {
        this.address = address;
    }

    public List<CategoryList> getCategories() {
        return categories;
    }

    public void setCategories(final List<CategoryList> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "RestaurantDetailsResponse{" +
               "id=" + id +
               ", restaurantName='" + restaurantName + '\'' +
               ", photoURL='" + photoURL + '\'' +
               ", customerRating=" + customerRating +
               ", averagePrice=" + averagePrice +
               ", numberCustomersRated=" + numberCustomersRated +
               ", address=" + address +
               ", categories=" + categories +
               '}';
    }
}
