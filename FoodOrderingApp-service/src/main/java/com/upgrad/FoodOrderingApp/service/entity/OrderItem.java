package com.upgrad.FoodOrderingApp.service.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "order_item")
@NamedQueries({
                  @NamedQuery(name = "orderItemByOrderId",query = "Select oe from OrderItem oe where oe.order.id = :id")
})
public class OrderItem implements Serializable {

    private static final long serialVersionUID = -4095515791779239352L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @Column(name = "quantity")
    private Integer quantity;
    
    public OrderEntity getOrder() {
        return order;
    }
    
    public void setOrder(OrderEntity order) {
        this.order = order;
    }
    
    public ItemEntity getItem() {
        return item;
    }
    
    public void setItem(ItemEntity item) {
        this.item = item;
    }
    
    @Column(name = "price")
    private Integer price;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(final Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(final Integer price) {
        this.price = price;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderItem)) {
            return false;
        }
        OrderItem orderItem = (OrderItem) o;
        return id.equals(orderItem.id) && order.equals(orderItem.order) && item.equals(
            orderItem.item)
                   && quantity.equals(orderItem.quantity) && price.equals(orderItem.price);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, order, item, quantity, price);
    }
}
