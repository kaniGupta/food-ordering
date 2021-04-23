package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponse;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    private static final Logger log = LoggerFactory.getLogger(RestaurantController.class);
    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(final RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public List<RestaurantDetailsResponse> getRestaurants() {
        log.debug("Get all restaurants.");
         restaurantService.getRestaurants();
         return null;
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
