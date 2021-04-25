package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.mapper.CategoryMapper;
import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.entity.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryController(final CategoryService categoryService,
                              final CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping
    public ResponseEntity<List<CategoryListResponse>> getCategories() {
        log.debug("Get all categories");
        final List<CategoryListResponse> responseList = new ArrayList<>();
        final List<Category> categories = categoryService.getCategories();

        if (null != categories && !categories.isEmpty()) {
            categories.forEach(category -> {
                responseList.add(categoryMapper.mapCategoryToCategoryDetailsResponse(category));
            });
            log.info("Total number of categories {}", categories.size());
        }
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{category_id}")
    public String getCategoryById(@PathVariable("category_id") final String categoryId) {
        return "CategoryController";
    }
}
