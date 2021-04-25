package com.upgrad.FoodOrderingApp.service.beans;

import java.util.List;

public class CategoryList {
    private String id;
    private String categoryName;
    private List<ItemList> itemList;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
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

    @Override
    public String toString() {
        return "CategoryList{" +
               "id=" + id +
               ", categoryName='" + categoryName + '\'' +
               ", itemList=" + itemList +
               '}';
    }
}
