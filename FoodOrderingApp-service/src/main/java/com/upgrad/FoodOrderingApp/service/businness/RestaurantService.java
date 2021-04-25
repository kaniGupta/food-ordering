package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.beans.RestaurantDetailsResponse;
import com.upgrad.FoodOrderingApp.service.beans.RestaurantDetailsResponseAddress;
import com.upgrad.FoodOrderingApp.service.beans.RestaurantDetailsResponseAddressState;
import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.Address;
import com.upgrad.FoodOrderingApp.service.entity.Category;
import com.upgrad.FoodOrderingApp.service.entity.Restaurant;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategory;
import com.upgrad.FoodOrderingApp.service.entity.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RestaurantService {

    private static final Logger log = LoggerFactory.getLogger(RestaurantService.class);
    private final RestaurantDao restaurantDao;
    private final StateDao stateDao;
    private final RestaurantCategoryDao restaurantCategoryDao;
    private final CategoryDao categoryDao;
    private final AddressDao addressDao;

    @Autowired
    public RestaurantService(final RestaurantDao restaurantDao,
                             final StateDao stateDao,
                             final RestaurantCategoryDao restaurantCategoryDao,
                             final CategoryDao categoryDao,
                             final AddressDao addressDao) {
        this.restaurantDao = restaurantDao;
        this.stateDao = stateDao;
        this.restaurantCategoryDao = restaurantCategoryDao;
        this.categoryDao = categoryDao;
        this.addressDao = addressDao;
    }

    public List<Restaurant> getRestaurants() {
        log.debug("Fetch Restaurants.");
        return restaurantDao.getAllRestaurants();
    }

    public String getRestaurantCategories(final Restaurant restaurant) {
        final List<RestaurantCategory> categories =
                restaurantCategoryDao.getRestaurantCategoriesByRestaurantId(restaurant.getId());
        final List<String> categoryList = new ArrayList<>();
        if (null != categories && !categories.isEmpty()) {
            categories.forEach(cat -> {
                final Category category = categoryDao.getCategoryById(cat.getId());
                if (null != category) {
                    categoryList.add(category.getCategoryName());
                }
            });
        }
        return String.join(", ", categoryList);
    }

    public Address getRestaurantAddress(final Integer id) {
        return addressDao.getAddressById(id);
    }

    public RestaurantDetailsResponse getRestaurantByName(final String restaurantName) {
        log.debug("Fetch Restaurant By Name.");
        final Restaurant restaurant = restaurantDao.getRestaurantByName(restaurantName);
        return getRestaurantDetailsResponse(restaurant);
    }

    public RestaurantDetailsResponse getRestaurantByUuid(final String uuid) {
        log.debug("Fetch Restaurant By Uuid.");
        final Restaurant restaurant = restaurantDao.getRestaurantByUuid(uuid);
        return getRestaurantDetailsResponse(restaurant);
    }

    private RestaurantDetailsResponse getRestaurantDetailsResponse(final Restaurant restaurant) {
        final RestaurantDetailsResponse response = new RestaurantDetailsResponse();
        if (null != restaurant) {
            final Address address = restaurant.getAddress();
            if (null != address && null != address.getId()) {
                final Integer id = restaurant.getAddress().getId();
                final State state = stateDao.getStateById(id);

                final RestaurantDetailsResponseAddress responseAddress = new RestaurantDetailsResponseAddress();
                final RestaurantDetailsResponseAddressState responseState =
                        new RestaurantDetailsResponseAddressState();

                responseState.setId(UUID.fromString(state.getUuid()));
                responseState.setStateName(state.getStateName());

                responseAddress.setId(UUID.fromString(address.getUuid()));
                responseAddress.setFlatBuildingName(address.getFlatBuilNumber());
                responseAddress.setLocality(address.getLocality());
                responseAddress.setCity(address.getCity());
                responseAddress.setPincode(address.getPincode());
                responseAddress.setState(responseState);

                response.setAddress(responseAddress);
            }
            response.setId(restaurant.getUuid());
            response.setRestaurantName(restaurant.getRestaurantName());
            response.setPhotoURL(restaurant.getPhotoUrl());
            response.setCustomerRating(restaurant.getCustomerRating());
            response.setAveragePrice(restaurant.getAveragePriceForTwo());
            response.setNumberCustomersRated(restaurant.getNumberOfCustomersRated());
        }
        return response;
    }
}
