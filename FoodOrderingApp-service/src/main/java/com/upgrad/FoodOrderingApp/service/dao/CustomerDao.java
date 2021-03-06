package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CustomerDao {


    @PersistenceContext
    private EntityManager entityManager;

    public CustomerEntity createCustomer(CustomerEntity CustomerEntity) {
        entityManager.persist(CustomerEntity);
        return CustomerEntity;
    }

    @Transactional
    public CustomerEntity getCustomerByUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("customerByUuid", CustomerEntity.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult();
        } catch (final NoResultException nre) {
            return null;
        }
    }

    public CustomerEntity getCustomerByEmail(final String email) {
        try {
            return entityManager.createNamedQuery("customerByEmail", CustomerEntity.class)
                    .setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public CustomerEntity getCustomerByContactNumber(final String contactNumber) {
        try {
            return entityManager.createNamedQuery("customerByContactNumber", CustomerEntity.class)
                    .setParameter("contactNumber", contactNumber).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    
    
    @Transactional
    public CustomerAuthEntity createAuthToken(final CustomerAuthEntity CustomerAuth) {
        entityManager.persist(CustomerAuth);
        return CustomerAuth;
    }
    
    @Transactional
    public void updateCustomer(final CustomerEntity updatedCustomerEntity) {
        entityManager.merge(updatedCustomerEntity);
    }
    
    @Transactional
    public void updateCustomerAuthEntity(final CustomerAuthEntity updatedCustomerAuth) {
        entityManager.merge(updatedCustomerAuth);
    }
    
    @Transactional
    public CustomerAuthEntity getCustomerAuthToken(final String accessToken) {
        try {
            return entityManager
                    .createNamedQuery("customerAuthByAccessToken", CustomerAuthEntity.class)
                    .setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @OnDelete(action = OnDeleteAction.CASCADE)
    @Transactional
    public void deleteCustomer(final CustomerEntity CustomerEntity) {
        entityManager.remove(CustomerEntity);
    }

}
