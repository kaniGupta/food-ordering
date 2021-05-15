package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.service.businness.ItemService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
public class ItemController {
    
    
    private RestaurantService restaurantService;
    
    @Autowired
    public ItemController(
        RestaurantService restaurantService,
        ItemService itemService) {
        this.restaurantService = restaurantService;
        this.itemService = itemService;
    }
    
    private ItemService itemService;
    
    

    @GetMapping("/restaurant/{restaurant_id}")
    public String getItemsByPopularity(@PathVariable("restaurant_id") final String restaurantId)
        throws RestaurantNotFoundException {
        if(restaurantId == null || StringUtils.isEmpty(restaurantId)) {
            throw new RestaurantNotFoundException("RNF-001","No restaurant by this id");
        }
        RestaurantEntity restaurantEntity = restaurantService.restaurantByUUID(restaurantId);
        return "ItemController";
    }
}
