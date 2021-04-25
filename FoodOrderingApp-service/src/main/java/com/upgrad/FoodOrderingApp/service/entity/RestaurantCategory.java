package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "restaurant_category")
@NamedQueries({
                      @NamedQuery(name = "getRestaurantCategoryByRestaurantId",
                                  query = "select r from RestaurantCategory r where r.restaurantId =:restaurantId")
              })
public class RestaurantCategory implements Serializable {

    private static final long serialVersionUID = -6235418440908751416L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "restaurant_id")
    private Integer restaurantId;

    @Column(name = "category_id")
    private Integer categoryId;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(final Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(final Integer categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RestaurantCategory)) {
            return false;
        }

        final RestaurantCategory that = (RestaurantCategory) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
            return false;
        }
        if (getRestaurantId() != null ? !getRestaurantId().equals(that.getRestaurantId()) :
            that.getRestaurantId() != null) {
            return false;
        }
        return getCategoryId() != null ? getCategoryId().equals(that.getCategoryId()) : that.getCategoryId() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getRestaurantId() != null ? getRestaurantId().hashCode() : 0);
        result = 31 * result + (getCategoryId() != null ? getCategoryId().hashCode() : 0);
        return result;
    }
}
