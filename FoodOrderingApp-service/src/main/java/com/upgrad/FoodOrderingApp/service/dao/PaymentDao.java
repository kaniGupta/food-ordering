package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import java.util.List;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class PaymentDao {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public List<PaymentEntity> getAllPaymentMethods() {
        try {
            return entityManager.createQuery("select p from PaymentEntity p").getResultList();
        } catch (final NoResultException nre) {
            return null;
        }
    }
    
}
