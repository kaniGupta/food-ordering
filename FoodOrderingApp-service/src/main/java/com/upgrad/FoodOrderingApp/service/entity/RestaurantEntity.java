package com.upgrad.FoodOrderingApp.service.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "restaurant")
@NamedQueries({
                      @NamedQuery(name = "allRestaurants", query = "select r from RestaurantEntity r"),
                      @NamedQuery(name = "getRestaurantsByName",
                                  query = "select r from RestaurantEntity r where r.restaurantName like :restaurantName"),
                      @NamedQuery(name = "getRestaurantsByUuid",
                                  query = "select r from RestaurantEntity r where r.uuid =:uuid")
              })
public class RestaurantEntity implements Serializable {
    private static final long serialVersionUID = 7449929436134115185L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "restaurant_name")
    @Size(max = 50)
    private String restaurantName;

    @Column(name = "photo_url")
    @Size(max = 255)
    private String photoUrl;

    @Column(name = "customer_rating")
    private Double customerRating;

    @Column(name = "average_price_for_two")
    private Integer averagePriceForTwo;

    @Column(name = "number_of_customers_rated")
    private Integer numberOfCustomersRated;
    
    public List<CategoryEntity> getCategories() {
        return categories;
    }
    
    public void setCategories(
        List<CategoryEntity> categories) {
        this.categories = categories;
    }
    
    @OneToOne
    @JoinColumn(name = "address_id")
    private AddressEntity addressEntity;
    
    
    @ManyToMany(targetEntity = CategoryEntity.class, cascade = {
        CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
    })
    @JoinTable(name = "RESTAURANT_CATEGORY",
        joinColumns = @JoinColumn(name = "RESTAURANT_ID", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID"))
    private List<CategoryEntity> categories = new ArrayList<>();
    

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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(final String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Double getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(final double customerRating) {
        this.customerRating = customerRating;
    }

    public Integer getAveragePriceForTwo() {
        return averagePriceForTwo;
    }

    public void setAveragePriceForTwo(final Integer averagePriceForTwo) {
        this.averagePriceForTwo = averagePriceForTwo;
    }

    public Integer getNumberOfCustomersRated() {
        return numberOfCustomersRated;
    }

    public void setNumberOfCustomersRated(final Integer numberOfCustomersRated) {
        this.numberOfCustomersRated = numberOfCustomersRated;
    }

    public AddressEntity getAddress() {
        return addressEntity;
    }

    public void setAddress(final AddressEntity addressEntity) {
        this.addressEntity = addressEntity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RestaurantEntity)) {
            return false;
        }

        final RestaurantEntity that = (RestaurantEntity) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
            return false;
        }
        if (getUuid() != null ? !getUuid().equals(that.getUuid()) : that.getUuid() != null) {
            return false;
        }
        if (getRestaurantName() != null ? !getRestaurantName().equals(that.getRestaurantName()) :
            that.getRestaurantName() != null) {
            return false;
        }
        if (getPhotoUrl() != null ? !getPhotoUrl().equals(that.getPhotoUrl()) : that.getPhotoUrl() != null) {
            return false;
        }
        if (getCustomerRating() != null ? !getCustomerRating().equals(that.getCustomerRating()) :
            that.getCustomerRating() != null) {
            return false;
        }
        if (getAveragePriceForTwo() != null ? !getAveragePriceForTwo().equals(that.getAveragePriceForTwo()) :
            that.getAveragePriceForTwo() != null) {
            return false;
        }
        if (getNumberOfCustomersRated() != null ?
            !getNumberOfCustomersRated().equals(that.getNumberOfCustomersRated()) :
            that.getNumberOfCustomersRated() != null) {
            return false;
        }
        return getAddress() != null ? getAddress().equals(that.getAddress()) : that.getAddress() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getUuid() != null ? getUuid().hashCode() : 0);
        result = 31 * result + (getRestaurantName() != null ? getRestaurantName().hashCode() : 0);
        result = 31 * result + (getPhotoUrl() != null ? getPhotoUrl().hashCode() : 0);
        result = 31 * result + (getCustomerRating() != null ? getCustomerRating().hashCode() : 0);
        result = 31 * result + (getAveragePriceForTwo() != null ? getAveragePriceForTwo().hashCode() : 0);
        result = 31 * result + (getNumberOfCustomersRated() != null ? getNumberOfCustomersRated().hashCode() : 0);
        result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
               "id=" + id +
               ", uuid='" + uuid + '\'' +
               ", restaurantName='" + restaurantName + '\'' +
               ", photoUrl='" + photoUrl + '\'' +
               ", customerRating=" + customerRating +
               ", averagePriceForTwo=" + averagePriceForTwo +
               ", numberOfCustomersRated=" + numberOfCustomersRated +
               ", address=" + addressEntity +
               '}';
    }
}
