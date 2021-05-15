package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CouponDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.entity.Coupon;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderItem;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    
    private final AddressDao addressDao;
    private final CustomerDao customerDao;
    private final OrderDao orderDao;
    private final CouponDao couponDao;
    
    @Autowired
    public OrderService(AddressDao addressDao,
        CustomerDao customerDao, OrderDao orderDao,
        CouponDao couponDao) {
        this.addressDao = addressDao;
        this.customerDao = customerDao;
        this.orderDao = orderDao;
        this.couponDao = couponDao;
    }
    
    public Coupon getCouponByCouponId(String uuid) {
        return couponDao.getCouponByCouponId(uuid);
    }
    
    public Coupon getCouponByCouponName(String couponName) {
        return couponDao.getCouponByCouponName(couponName);
    }
    
    public OrderEntity saveOrder(OrderEntity orderEntity) {
        return orderDao.save(orderEntity);
    }
    
    public void saveOrderItem(OrderItem orderItem) {
        orderDao.saveOrderItem(orderItem);
    }
    
    public List<OrderItem> getOrderItemsByOrderId(Integer id) {
        return orderDao.getOrderItemsByOrderId(id);
    }
    
    public List<OrderEntity> getOrdersByCustomers(String customerId) {
         return orderDao.getOrdersOfCustomer(customerId);
    }
}
