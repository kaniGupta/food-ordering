package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<CategoryEntity> getAllCategories() {
        try {
            return entityManager.createNamedQuery("allCategory", CategoryEntity.class)
                                .getResultList();
        } catch (final NoResultException nre) {
            return null;
        }
    }

    public CategoryEntity getCategoryByUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("getCategoryByUuid", CategoryEntity.class)
                                .setParameter("uuid", uuid)
                                .getSingleResult();
        } catch (final NoResultException nre) {
            return null;
        }
    }

    public CategoryEntity getCategoryById(final Integer id) {
        try {
            return entityManager.createNamedQuery("getCategoryById", CategoryEntity.class)
                                .setParameter("id", id)
                                .getSingleResult();
        } catch (final NoResultException nre) {
            return null;
        }
    }
}
