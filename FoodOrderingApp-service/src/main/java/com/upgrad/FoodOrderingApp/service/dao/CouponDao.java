package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.Coupon;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class CouponDao {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public Coupon getCouponByCouponId(String couponId){
        try {
            return entityManager.createNamedQuery("couponByUuid", Coupon.class)
                       .setParameter("uuid", couponId)
                       .getSingleResult();
        } catch (final NoResultException nre) {
            return null;
        }
    }
    
    public Coupon getCouponByCouponName(String couponName){
        try {
            return entityManager.createNamedQuery("couponByName", Coupon.class)
                       .setParameter("couponName", couponName)
                       .getSingleResult();
        } catch (final NoResultException nre) {
            return null;
        }
    }
    
    
}
