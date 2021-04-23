package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.service.businness.CustomerBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerBusinessService customerBusinessService;

    @Autowired
    public CustomerController(final CustomerBusinessService customerBusinessService) {
        this.customerBusinessService = customerBusinessService;
    }


}
