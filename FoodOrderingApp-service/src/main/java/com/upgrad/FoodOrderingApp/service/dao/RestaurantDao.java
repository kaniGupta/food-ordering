package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.Restaurant;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Restaurant> getAllRestaurants() {
        try {
            return entityManager.createNamedQuery("allRestaurants", Restaurant.class)
                                .getResultList();
        } catch (final NoResultException nre) {
            return null;
        }
    }

    public Restaurant getRestaurantByName(final String restaurantName) {
        try {
            return entityManager.createNamedQuery("getRestaurantsByName", Restaurant.class)
                                .setParameter("restaurantName", restaurantName)
                                .getSingleResult();
        } catch (final NoResultException nre) {
            return null;
        }
    }
}
