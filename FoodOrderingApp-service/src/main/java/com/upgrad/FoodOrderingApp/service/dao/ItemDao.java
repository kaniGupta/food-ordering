package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class ItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    public ItemEntity getItemById(final Integer id) {
        try {
            return entityManager.createNamedQuery("getItemById", ItemEntity.class)
                                .setParameter("id", id)
                                .getSingleResult();
        } catch (final NoResultException nre) {
            return null;
        }
    }
    
    public ItemEntity getItemByUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("getItemByUuid", ItemEntity.class)
                       .setParameter("uuid", uuid)
                       .getSingleResult();
        } catch (final NoResultException nre) {
            return null;
        }
    }
}
