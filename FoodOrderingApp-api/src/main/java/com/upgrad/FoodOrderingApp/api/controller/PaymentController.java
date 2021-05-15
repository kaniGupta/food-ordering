package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.PaymentListResponse;
import com.upgrad.FoodOrderingApp.api.model.PaymentResponse;
import com.upgrad.FoodOrderingApp.service.businness.PaymentService;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    
    private final PaymentService paymentService;
    
    @Autowired
    public PaymentController(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/payment", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CrossOrigin
    public ResponseEntity<PaymentListResponse> getAllPaymentMethods() {
        PaymentListResponse list = convertPaymentModel(paymentService.getAllPaymentMethods());
        return new ResponseEntity<PaymentListResponse>(list, HttpStatus.OK);
    }
    
    private PaymentListResponse convertPaymentModel(List<PaymentEntity> payments) {
        if(payments == null || CollectionUtils.isEmpty(payments)) {
            return null;
        }
        PaymentListResponse list = new PaymentListResponse();
        PaymentResponse response;
        for(PaymentEntity payment:payments) {
            response = new PaymentResponse();
            response.setId(UUID.fromString(payment.getUuid()));
            response.setPaymentName(payment.getPaymentName());
            list.addPaymentMethodsItem(response);
        }
        return list;
    }
}
