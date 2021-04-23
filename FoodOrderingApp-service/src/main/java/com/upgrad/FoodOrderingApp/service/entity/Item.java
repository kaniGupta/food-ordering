package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "item")
public class Item implements Serializable {

    private static final long serialVersionUID = -7657675664048211124L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "item_name")
    @Size(max = 30)
    private String itemName;

    @Column(name = "price")
    private Integer price;

    @Column(name = "type")
    @Size(max = 10)
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(final String uuid) {
        this.uuid = uuid;
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

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }

        final Item item = (Item) o;

        if (getId() != null ? !getId().equals(item.getId()) : item.getId() != null) {
            return false;
        }
        if (getUuid() != null ? !getUuid().equals(item.getUuid()) : item.getUuid() != null) {
            return false;
        }
        if (getItemName() != null ? !getItemName().equals(item.getItemName()) : item.getItemName() != null) {
            return false;
        }
        if (getPrice() != null ? !getPrice().equals(item.getPrice()) : item.getPrice() != null) {
            return false;
        }
        return getType() != null ? getType().equals(item.getType()) : item.getType() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getUuid() != null ? getUuid().hashCode() : 0);
        result = 31 * result + (getItemName() != null ? getItemName().hashCode() : 0);
        result = 31 * result + (getPrice() != null ? getPrice().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        return result;
    }
}
