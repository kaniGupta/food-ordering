package com.upgrad.FoodOrderingApp.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @GetMapping
    public String getCategories() {
        return "CategoryController";
    }

    @GetMapping("/{category_id}")
    public String getCategoryById(@PathVariable("category_id") final String categoryId) {
        return "CategoryController";
    }
}
