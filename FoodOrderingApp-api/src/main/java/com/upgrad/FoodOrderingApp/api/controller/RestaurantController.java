package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.mapper.RestaurantMapper;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponse;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    private static final Logger log = LoggerFactory.getLogger(RestaurantController.class);
    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;

    @Autowired
    public RestaurantController(final RestaurantService restaurantService,
                                final RestaurantMapper restaurantMapper) {
        this.restaurantService = restaurantService;
        this.restaurantMapper = restaurantMapper;
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDetailsResponse>> getRestaurants() {
        log.debug("Get all restaurants.");
        final List<RestaurantDetailsResponse> responseList = new ArrayList<>();
        final List<com.upgrad.FoodOrderingApp.service.beans.RestaurantDetailsResponse> restaurants =
                restaurantService.getRestaurants();
        if (null != restaurants && !restaurants.isEmpty()) {
            restaurants.forEach(restaurant -> {
                final RestaurantDetailsResponse response =
                        restaurantMapper.mapRestaurantDetailsResponse(restaurant);
                if (null != response) {
                    responseList.add(response);
                }
            });
        }
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/name/{restaurant_name}")
    public ResponseEntity<RestaurantDetailsResponse> getRestaurantsByName(
            @PathVariable("restaurant_name") final String restaurantName) {
        log.debug("Get restaurant by name {}.", restaurantName);
        final com.upgrad.FoodOrderingApp.service.beans.RestaurantDetailsResponse restaurant =
                restaurantService.getRestaurantByName(restaurantName);
        final RestaurantDetailsResponse response =
                restaurantMapper.mapRestaurantDetailsResponse(restaurant);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{category_id}")
    public String getRestaurantsByCategory(@PathVariable("category_id") final String categoryId) {
        return "Welcome";
    }

    @GetMapping("/{restaurant_id}")
    public ResponseEntity<RestaurantDetailsResponse> getRestaurantsByRestaurantId(
            @PathVariable("restaurant_id") final String restaurantId) {
        log.debug("Get restaurant by Uuid {}.", restaurantId);
        final com.upgrad.FoodOrderingApp.service.beans.RestaurantDetailsResponse restaurant =
                restaurantService.getRestaurantByUuid(restaurantId);
        final RestaurantDetailsResponse response =
                restaurantMapper.mapRestaurantDetailsResponse(restaurant);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{restaurant_id}")
    public String updateRestaurantsDetails(@PathVariable("restaurant_id") final String restaurantId) {
        return "Welcome";
    }
}
