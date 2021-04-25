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
@Table(name = "category_item")
@NamedQueries({
                      @NamedQuery(name = "getCategoryItemByCategoryId",
                                  query = "select c from CategoryItem c where c.categoryId =:categoryId")
              })
public class CategoryItem implements Serializable {
    private static final long serialVersionUID = -5281420425861762932L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "category_id")
    private Integer categoryId;

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
        if (!(o instanceof CategoryItem)) {
            return false;
        }

        final CategoryItem that = (CategoryItem) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
            return false;
        }
        if (getItemId() != null ? !getItemId().equals(that.getItemId()) : that.getItemId() != null) {
            return false;
        }
        return getCategoryId() != null ? getCategoryId().equals(that.getCategoryId()) : that.getCategoryId() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getItemId() != null ? getItemId().hashCode() : 0);
        result = 31 * result + (getCategoryId() != null ? getCategoryId().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CategoryItem{" +
               "id=" + id +
               ", itemId=" + itemId +
               ", categoryId=" + categoryId +
               '}';
    }
}
