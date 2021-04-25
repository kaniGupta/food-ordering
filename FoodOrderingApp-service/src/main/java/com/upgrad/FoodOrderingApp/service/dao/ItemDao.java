package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.Item;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class ItemDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Item getItemById(final Integer id) {
        try {
            return entityManager.createNamedQuery("getItemById", Item.class)
                                .setParameter("id", id)
                                .getSingleResult();
        } catch (final NoResultException nre) {
            return null;
        }
    }
}
