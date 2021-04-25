package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryItem;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<CategoryItem> getCategoryItemsByCategoryId(final Integer categoryId) {
        try {
            return entityManager.createNamedQuery("getCategoryItemByCategoryId", CategoryItem.class)
                                .setParameter("categoryId", categoryId)
                                .getResultList();
        } catch (final NoResultException nre) {
            return null;
        }
    }
}
