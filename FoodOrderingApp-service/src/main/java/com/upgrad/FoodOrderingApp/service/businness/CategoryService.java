package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.entity.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryDao categoryDao;

    @Autowired
    public CategoryService(final CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public List<Category> getCategories() {
        log.debug("Get all categories");
        return categoryDao.getAllCategories();
    }
}
