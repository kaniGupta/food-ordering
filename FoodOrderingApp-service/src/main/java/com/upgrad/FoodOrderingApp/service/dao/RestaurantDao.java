package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class RestaurantDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<RestaurantEntity> getAllRestaurants() {
        try {
            return entityManager.createNamedQuery("allRestaurants", RestaurantEntity.class)
                                .getResultList();
        } catch (final NoResultException nre) {
            return null;
        }
    }

    public List<RestaurantEntity> getRestaurantByName(final String restaurantName) {
        try {
            return entityManager.createQuery("select r from RestaurantEntity r where r.restaurantName like :restaurantName")
                                .setParameter("restaurantName", "%"+restaurantName+"%")
                                .getResultList();
        } catch (final NoResultException nre) {
            return null;
        }
    }

    public RestaurantEntity getRestaurantByUUID(final String uuid) {
        try {
            return entityManager.createNamedQuery("getRestaurantsByUuid", RestaurantEntity.class)
                                .setParameter("uuid", uuid)
                                .getSingleResult();
        } catch (final NoResultException nre) {
            return null;
        }
    }
    
    @Transactional
    public void updateRestaurant(final RestaurantEntity restaurantEntity) {
        entityManager.merge(restaurantEntity);
    }
}
