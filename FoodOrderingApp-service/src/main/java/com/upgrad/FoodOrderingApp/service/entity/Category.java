package com.upgrad.FoodOrderingApp.service.entity;

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
@Table(name = "category")
@NamedQueries({
                      @NamedQuery(name = "allCategory", query = "select c from Category c"),
                      @NamedQuery(name = "getCategoryByUuid",
                                  query = "select r from Category r where r.uuid =:uuid"),
                      @NamedQuery(name = "getCategoryById",
                                  query = "select r from Category r where r.id =:id")
              })
public class Category implements Serializable {
    private static final long serialVersionUID = 2883504702565284914L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "category_name")
    @Size(max = 255)
    private String categoryName;

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
        if (!(o instanceof Category)) {
            return false;
        }

        final Category category = (Category) o;

        if (getId() != null ? !getId().equals(category.getId()) : category.getId() != null) {
            return false;
        }
        if (getUuid() != null ? !getUuid().equals(category.getUuid()) : category.getUuid() != null) {
            return false;
        }
        return getCategoryName() != null ? getCategoryName().equals(category.getCategoryName()) :
               category.getCategoryName() == null;
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
