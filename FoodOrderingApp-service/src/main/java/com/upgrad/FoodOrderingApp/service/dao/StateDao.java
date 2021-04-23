package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.State;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class StateDao {

    @PersistenceContext
    private EntityManager entityManager;

    public State getStateById(final Integer id) {
        try {
            return entityManager.createNamedQuery("stateById", State.class)
                                .setParameter("id", id)
                                .getSingleResult();
        } catch (final NoResultException nre) {
            return null;
        }
    }
}
