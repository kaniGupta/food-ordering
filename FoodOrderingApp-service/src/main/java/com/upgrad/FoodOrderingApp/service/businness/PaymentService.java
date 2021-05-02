package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.PaymentDao;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    
    private PaymentDao paymentDao;
    
    @Autowired
    public PaymentService(PaymentDao paymentDao) {
        this.paymentDao = paymentDao;
    }
    
    public List<PaymentEntity> getAllPaymentMethods() {
        return paymentDao.getAllPaymentMethods();
    }
}
