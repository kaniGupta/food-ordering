package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuth;
import com.upgrad.FoodOrderingApp.service.entity.Customer;
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

    public Customer createCustomer(Customer Customer) {
        entityManager.persist(Customer);
        return Customer;
    }

    @Transactional
    public Customer getCustomerByUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("customerByUuid", Customer.class)
                    .setParameter("uuid", uuid)
                    .getSingleResult();
        } catch (final NoResultException nre) {
            return null;
        }
    }

    public Customer getCustomerByEmail(final String email) {
        try {
            return entityManager.createNamedQuery("customerByEmail", Customer.class)
                    .setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public Customer getCustomerByContactNumber(final String contactNumber) {
        try {
            return entityManager.createNamedQuery("customerByContactNumber", Customer.class)
                    .setParameter("contactNumber", contactNumber).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
    
    
    @Transactional
    public CustomerAuth createAuthToken(final CustomerAuth CustomerAuth) {
        entityManager.persist(CustomerAuth);
        return CustomerAuth;
    }
    
    @Transactional
    public void updateCustomer(final Customer updatedCustomer) {
        entityManager.merge(updatedCustomer);
    }
    
    @Transactional
    public void updateCustomerAuthEntity(final CustomerAuth updatedCustomerAuth) {
        entityManager.merge(updatedCustomerAuth);
    }
    
    @Transactional
    public CustomerAuth getCustomerAuthToken(final String accessToken) {
        try {
            return entityManager
                    .createNamedQuery("customerAuthByAccessToken", CustomerAuth.class)
                    .setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    @OnDelete(action = OnDeleteAction.CASCADE)
    @Transactional
    public void deleteCustomer(final Customer Customer) {
        entityManager.remove(Customer);
    }

}
