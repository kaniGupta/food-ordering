package com.upgrad.FoodOrderingApp.api.mapper;

import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CategoryMapper {

    public CategoryListResponse mapCategoryToCategoryDetailsResponse(final CategoryEntity categoryEntity) {
        final CategoryListResponse response = new CategoryListResponse();
        if (null != categoryEntity) {
            response.setId(UUID.fromString(categoryEntity.getUuid()));
            response.setCategoryName(categoryEntity.getCategoryName());
        }
        return response;
    }
}
