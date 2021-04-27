package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class StateDao {

    @PersistenceContext
    private EntityManager entityManager;

    public StateEntity getStateById(final Integer id) {
        try {
            return entityManager.createNamedQuery("stateById", StateEntity.class)
                                .setParameter("id", id)
                                .getSingleResult();
        } catch (final NoResultException nre) {
            return null;
        }
    }
    
    public StateEntity getStateByUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("stateByUuid", StateEntity.class)
                       .setParameter("uuid", uuid)
                       .getSingleResult();
        } catch (final NoResultException nre) {
            return null;
        }
    }
    
    public List<StateEntity> getAllStates() {
        try {
            return entityManager.createQuery("select s from StateEntity s").getResultList();
        } catch (final NoResultException nre) {
            return null;
        }
    }
}
