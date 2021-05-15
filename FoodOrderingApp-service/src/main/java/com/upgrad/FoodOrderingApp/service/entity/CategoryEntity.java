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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "category")
@NamedQueries({
                      @NamedQuery(name = "allCategory", query = "select c from CategoryEntity c"),
                      @NamedQuery(name = "getCategoryByUuid", query = "select r from CategoryEntity r where r.uuid =:uuid"),
                      @NamedQuery(name = "getCategoryById", query = "select r from CategoryEntity r where r.id =:id")
              })
public class CategoryEntity implements Serializable {
    private static final long serialVersionUID = 2883504702565284914L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @NotNull
    @Size(max = 200)
    private String uuid;
    
    public List<ItemEntity> getItems() {
        return items;
    }
    
    public void setItems(List<ItemEntity> items) {
        this.items = items;
    }
    
    @Column(name = "category_name")
    @Size(max = 255)
    private String categoryName;
    
    @ManyToMany(targetEntity = ItemEntity.class, cascade = {
        CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
    })
    @JoinTable(name = "CATEGORY_ITEM",
        joinColumns = @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID"),
        inverseJoinColumns = @JoinColumn(name = "ITEM_ID", referencedColumnName = "ID"))
    private List<ItemEntity> items = new ArrayList<>();
    
    @ManyToMany(targetEntity = RestaurantEntity.class,mappedBy = "categories",cascade =  {
        CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH
    })
    private List<RestaurantEntity> restaurants = new ArrayList<>();
    
    public List<RestaurantEntity> getRestaurants() {
        return restaurants;
    }
    
    public void setRestaurants(
        List<RestaurantEntity> restaurants) {
        this.restaurants = restaurants;
    }
    
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(final String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoryEntity)) {
            return false;
        }

        final CategoryEntity categoryEntity = (CategoryEntity) o;

        if (getId() != null ? !getId().equals(categoryEntity.getId()) : categoryEntity.getId() != null) {
            return false;
        }
        if (getUuid() != null ? !getUuid().equals(categoryEntity.getUuid()) : categoryEntity.getUuid() != null) {
            return false;
        }
        return getCategoryName() != null ? getCategoryName().equals(categoryEntity.getCategoryName()) :
               categoryEntity.getCategoryName() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getUuid() != null ? getUuid().hashCode() : 0);
        result = 31 * result + (getCategoryName() != null ? getCategoryName().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Category{" +
               "id=" + id +
               ", uuid='" + uuid + '\'' +
               ", categoryName='" + categoryName + '\'' +
               '}';
    }
}
