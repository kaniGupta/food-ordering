package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.State;
import java.util.List;
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
    
    public State getStateByUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("stateByUuid", State.class)
                       .setParameter("uuid", uuid)
                       .getSingleResult();
        } catch (final NoResultException nre) {
            return null;
        }
    }
    
    public List<State> getAllStates() {
        try {
            return entityManager.createQuery("select s from State s").getResultList();
        } catch (final NoResultException nre) {
            return null;
        }
    }
}
