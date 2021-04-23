package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "restaurant_item")
public class RestaurantItem implements Serializable {

    private static final long serialVersionUID = 4661961279136840717L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "restaurant_id")
    private Integer restaurantId;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(final Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(final Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RestaurantItem)) {
            return false;
        }

        final RestaurantItem that = (RestaurantItem) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
            return false;
        }
        if (getItemId() != null ? !getItemId().equals(that.getItemId()) : that.getItemId() != null) {
            return false;
        }
        return getRestaurantId() != null ? getRestaurantId().equals(that.getRestaurantId()) :
               that.getRestaurantId() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getItemId() != null ? getItemId().hashCode() : 0);
        result = 31 * result + (getRestaurantId() != null ? getRestaurantId().hashCode() : 0);
        return result;
    }
}
