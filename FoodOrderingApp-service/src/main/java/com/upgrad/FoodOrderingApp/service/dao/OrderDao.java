package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.Coupon;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItem;
import java.util.Comparator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.hibernate.internal.util.ZonedDateTimeComparator;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class OrderDao {
    @PersistenceContext
    private EntityManager entityManager;
    
    @Transactional
    public OrderEntity save(OrderEntity orderEntity) {
        entityManager.persist(orderEntity);
        return orderEntity;
    }
    
    public Coupon getCouponByCouponId(String couponId){
        try {
            return entityManager.createNamedQuery("couponByUuid", Coupon.class)
                       .setParameter("uuid", couponId)
                       .getSingleResult();
        } catch (final NoResultException nre) {
            return null;
        }
    }
    
    @Transactional
    public void saveOrderItem(OrderItem orderItem) {
        entityManager.persist(orderItem);
    }
    
    public List<OrderItem> getOrderItemsByOrderId(Integer id) {
        return entityManager.createNamedQuery("orderItemByOrderId", OrderItem.class)
                   .setParameter("id", id)
                   .getResultList();
    }
    
    public List<OrderEntity> getOrdersOfCustomer(String customerId){
        try {
            return entityManager.createNamedQuery("orderByCustomerUuid", OrderEntity.class)
                       .setParameter("uuid", customerId).getResultList();
        } catch (final NoResultException nre) {
            return null;
        }
    }
    
}
