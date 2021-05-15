package com.upgrad.FoodOrderingApp.service.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "coupon")
@NamedQueries({
    @NamedQuery(name = "couponByUuid", query = "select c from Coupon c where c.uuid = :uuid"),
    @NamedQuery(name = "couponByName", query = "select c from Coupon c where c.couponName = :couponName")
})
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
    private String couponName;

    @Column(name = "percent")
    private Integer percent;

    public Integer getId() {
        return id;
    }
    
    public String getCouponName() {
        return couponName;
    }
    
    public void setCouponName(String couponName) {
        this.couponName = couponName;
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

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(final Integer percent) {
        this.percent = percent;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Coupon)) {
            return false;
        }
        Coupon coupon = (Coupon) o;
        return id.equals(coupon.id) && uuid.equals(coupon.uuid) && couponName
                                                                       .equals(coupon.couponName)
                   && percent.equals(coupon.percent);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, couponName, percent);
    }
}
