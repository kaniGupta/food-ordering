package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.CategoryItemDao;
import com.upgrad.FoodOrderingApp.service.entity.Category;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryDao categoryDao;
    private final CategoryItemDao categoryItemDao;

    @Autowired
    public CategoryService(final CategoryDao categoryDao,
                           final CategoryItemDao categoryItemDao) {
        this.categoryDao = categoryDao;
        this.categoryItemDao = categoryItemDao;
    }

    public List<Category> getCategories() {
        log.info("Get all categories");
        return categoryDao.getAllCategories();
    }

    public Category getCategoryById(final String categoryId) {
        log.info("Get category by UUID : {}", categoryId);
        return categoryDao.getCategoryByUuid(categoryId);
    }

    public List<Integer> getItemIdsFromCategoryItem(final Integer id) {
        final List<CategoryItem> list = categoryItemDao.getCategoryItemsByCategoryId(id);
        if (null != list && !list.isEmpty()) {
            return list.stream().map(CategoryItem::getItemId).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
