package com.upgrad.FoodOrderingApp.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    @GetMapping
    public String getRestaurant() {
        return "Welcome";
    }

    @GetMapping("/name/{restaurant_name}")
    public String getRestaurantsByName(@PathVariable("restaurant_name") final String restaurantName) {
        return "Welcome";
    }

    @GetMapping("/category/{category_id}")
    public String getRestaurantsByCategory(@PathVariable("category_id") final String categoryId) {
        return "Welcome";
    }

    @GetMapping("/{restaurant_id}")
    public String getRestaurantsByRestaurantId(@PathVariable("restaurant_id") final String restaurantId) {
        return "Welcome";
    }

    @PutMapping("/{restaurant_id}")
    public String updateRestaurantsDetails(@PathVariable("restaurant_id") final String restaurantId) {
        return "Welcome";
    }
}
