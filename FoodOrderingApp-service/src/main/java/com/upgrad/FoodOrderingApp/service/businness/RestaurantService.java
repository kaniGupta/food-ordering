package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.beans.RestaurantDetailsResponse;
import com.upgrad.FoodOrderingApp.service.beans.RestaurantDetailsResponseAddress;
import com.upgrad.FoodOrderingApp.service.beans.RestaurantDetailsResponseAddressState;
import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantCategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategory;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import java.math.BigDecimal;
import java.util.Collections;
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

    public List<RestaurantEntity> getRestaurants() {
        log.debug("Fetch Restaurants.");
        return restaurantDao.getAllRestaurants();
    }

    public String getRestaurantCategories(final RestaurantEntity restaurantEntity) {
        final List<RestaurantCategory> categories =
                restaurantCategoryDao.getRestaurantCategoriesByRestaurantId(restaurantEntity.getId());
        final List<String> categoryList = new ArrayList<>();
        if (null != categories && !categories.isEmpty()) {
            categories.forEach(cat -> {
                final CategoryEntity categoryEntity = categoryDao.getCategoryById(cat.getId());
                if (null != categoryEntity) {
                    categoryList.add(categoryEntity.getCategoryName());
                }
            });
        }
        return String.join(", ", categoryList);
    }

    public AddressEntity getRestaurantAddress(final Integer id) {
        return addressDao.getAddressById(id);
    }

    public List<RestaurantEntity> restaurantsByName(final String restaurantName) {
        final List<RestaurantEntity> restaurantEntities = restaurantDao.getRestaurantByName(restaurantName);
        return restaurantEntities;
    }

    public RestaurantEntity restaurantByUUID(final String uuid) {
        final RestaurantEntity restaurantEntity = restaurantDao.getRestaurantByUUID(uuid);
        return restaurantEntity;
    }
    
    public List<RestaurantEntity> restaurantByCategory(String categoryId) {
        return Collections.emptyList();
    }
    
    public List<RestaurantEntity> restaurantsByRating() {
        return Collections.emptyList();
    }
    
    public RestaurantEntity updateRestaurantRating(RestaurantEntity restaurantEntity,double rating) {
        int numberOfCustomers = restaurantEntity.getNumberOfCustomersRated();
        double currentRating = restaurantEntity.getCustomerRating()*numberOfCustomers + rating;
        double newRating = (currentRating/(double)(numberOfCustomers+1));
        restaurantEntity.setCustomerRating(newRating);
        restaurantEntity.setNumberOfCustomersRated(numberOfCustomers+1);
        restaurantDao.updateRestaurant(restaurantEntity);
        return restaurantEntity;
    }
    
    
}
