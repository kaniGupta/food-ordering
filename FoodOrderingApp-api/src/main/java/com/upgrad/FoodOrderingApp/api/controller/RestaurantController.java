package com.upgrad.FoodOrderingApp.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    @GetMapping
    public String getRestaurant(){
        return "Welcome";
    }
}
