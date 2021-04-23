package com.upgrad.FoodOrderingApp.service.beans;

import java.util.UUID;

public class ItemList {

    private UUID id;

    private String itemName;

    private Integer price;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(final String itemName) {
        this.itemName = itemName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(final Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ItemList{" +
               "id=" + id +
               ", itemName='" + itemName + '\'' +
               ", price=" + price +
               '}';
    }
}
