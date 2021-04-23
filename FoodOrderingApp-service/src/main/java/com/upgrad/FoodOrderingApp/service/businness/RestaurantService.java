package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.Restaurant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private static final Logger log = LoggerFactory.getLogger(RestaurantService.class);
    private final RestaurantDao restaurantDao;

    @Autowired
    public RestaurantService(final RestaurantDao restaurantDao) {
        this.restaurantDao = restaurantDao;
    }

    public void getRestaurants() {

        final List<Restaurant> restaurants = restaurantDao.getAllRestaurants();
        log.info("Restaurants {}", restaurants.toString());
    }
}
