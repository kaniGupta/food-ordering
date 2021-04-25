package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.Category;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Category> getAllRestaurants() {
        try {
            return entityManager.createNamedQuery("allCategory", Category.class)
                                .getResultList();
        } catch (final NoResultException nre) {
            return null;
        }
    }
}
