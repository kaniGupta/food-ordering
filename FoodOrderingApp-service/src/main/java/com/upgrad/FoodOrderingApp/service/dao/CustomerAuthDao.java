package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuth;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class CustomerAuthDao {
    @PersistenceContext
    private EntityManager entityManager;

    public CustomerAuth getUserAuth(final String accessToken) {
        try {
            return entityManager
                    .createNamedQuery("customerAuthByAccessToken", CustomerAuth.class)
                    .setParameter("accessToken", accessToken)
                    .getSingleResult();
        } catch (final NoResultException nre) {
            return null;
        }
    }
}
