package com.upgrad.FoodOrderingApp.service.beans;

import java.util.List;
import java.util.UUID;

public class CategoryList {

    private UUID id;
    private String categoryName;
    private List<ItemList> itemList;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(final String categoryName) {
        this.categoryName = categoryName;
    }

    public List<ItemList> getItemList() {
        return itemList;
    }

    public void setItemList(final List<ItemList> itemList) {
        this.itemList = itemList;
    }
}
