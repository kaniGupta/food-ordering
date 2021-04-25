package com.upgrad.FoodOrderingApp.api.mapper;

import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.service.entity.Category;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CategoryMapper {

    public CategoryListResponse mapCategoryToCategoryDetailsResponse(final Category category) {
        final CategoryListResponse response = new CategoryListResponse();
        if (null != category) {
            response.setId(UUID.fromString(category.getUuid()));
            response.setCategoryName(category.getCategoryName());
        }
        return response;
    }
}
