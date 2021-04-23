package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.beans.RestaurantDetailsResponse;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.Restaurant;
import com.upgrad.FoodOrderingApp.service.entity.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private static final Logger log = LoggerFactory.getLogger(RestaurantService.class);
    private final RestaurantDao restaurantDao;
    private final StateDao stateDao;

    @Autowired
    public RestaurantService(final RestaurantDao restaurantDao,
                             final StateDao stateDao) {
        this.restaurantDao = restaurantDao;
        this.stateDao = stateDao;
    }

    public RestaurantDetailsResponse getRestaurants() {

        final List<Restaurant> restaurants = restaurantDao.getAllRestaurants();
        final State states = stateDao.getStateById(1);
        log.info("Restaurants {}", restaurants.toString());
        log.info("States {}", states);

        return null;
    }
}
