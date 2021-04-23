package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.Address;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class AddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Address getAddressById(final Integer id) {
        try {
            return entityManager.createNamedQuery("addressById", Address.class)
                                .setParameter("id", id)
                                .getSingleResult();
        } catch (final NoResultException nre) {
            return null;
        }
    }
}
