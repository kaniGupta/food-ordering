package com.upgrad.FoodOrderingApp.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
public class ItemController {

    @GetMapping("/restaurant/{restaurant_id}")
    public String getTopFiveItems(@PathVariable("restaurant_id") final String restaurantId) {
        return "ItemController";
    }
}
