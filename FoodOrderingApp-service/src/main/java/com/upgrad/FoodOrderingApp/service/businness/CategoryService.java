package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.CategoryItemDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItem;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.apache.commons.lang3.StringUtils;
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

    public List<CategoryEntity> getAllCategoriesOrderedByName() {
        return categoryDao.getAllCategories();
    }

    public CategoryEntity getCategoryById(final String categoryId)
        throws CategoryNotFoundException {
        log.info("Get category by UUID : {}", categoryId);
        if(categoryId == null || StringUtils.isEmpty(categoryId)) {
            throw new CategoryNotFoundException("CNF-001","Category id field should not be empty");
        }
        final CategoryEntity categoryEntity = categoryDao.getCategoryByUuid(categoryId);
        if(categoryEntity == null) {
            throw new CategoryNotFoundException("CNF-002","No category by this id");
        }
        return categoryEntity;
    }

    public List<Integer> getItemIdsFromCategoryItem(final Integer id) {
        final List<CategoryItem> list = categoryItemDao.getCategoryItemsByCategoryId(id);
        if (null != list && !list.isEmpty()) {
            return list.stream().map(CategoryItem::getItemId).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
    
    public List<CategoryEntity> getCategoriesByRestaurant(final String restaurantId) {
        return Collections.emptyList();
    }
}
