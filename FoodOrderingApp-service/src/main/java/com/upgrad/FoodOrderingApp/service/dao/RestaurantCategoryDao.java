package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.RestaurantCategory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantCategoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<RestaurantCategory> getRestaurantCategoriesByRestaurantId(final Integer restaurantId) {
        try {
            return entityManager.createNamedQuery("getRestaurantCategoryByRestaurantId", RestaurantCategory.class)
                                .setParameter("restaurantId", restaurantId)
                                .getResultList();
        } catch (final NoResultException nre) {
            return null;
        }
    }
}
