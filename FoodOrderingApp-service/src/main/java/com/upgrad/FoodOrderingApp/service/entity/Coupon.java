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
@Table(name = "coupon")
public class Coupon implements Serializable {

    private static final long serialVersionUID = -5138057655759304278L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "coupon_name")
    @Size(max = 255)
    private String restaurantName;

    @Column(name = "percent")
    private Integer percent;

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

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(final String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(final Integer percent) {
        this.percent = percent;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Coupon)) {
            return false;
        }

        final Coupon coupon = (Coupon) o;

        if (getId() != null ? !getId().equals(coupon.getId()) : coupon.getId() != null) {
            return false;
        }
        if (getUuid() != null ? !getUuid().equals(coupon.getUuid()) : coupon.getUuid() != null) {
            return false;
        }
        if (getRestaurantName() != null ? !getRestaurantName().equals(coupon.getRestaurantName()) :
            coupon.getRestaurantName() != null) {
            return false;
        }
        return getPercent() != null ? getPercent().equals(coupon.getPercent()) : coupon.getPercent() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getUuid() != null ? getUuid().hashCode() : 0);
        result = 31 * result + (getRestaurantName() != null ? getRestaurantName().hashCode() : 0);
        result = 31 * result + (getPercent() != null ? getPercent().hashCode() : 0);
        return result;
    }
}
