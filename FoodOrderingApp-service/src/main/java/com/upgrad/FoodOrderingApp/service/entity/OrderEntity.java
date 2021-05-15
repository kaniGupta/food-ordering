package com.upgrad.FoodOrderingApp.service.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "orders")
@NamedQueries({
    @NamedQuery(name = "orderByCustomerUuid", query = "select o from OrderEntity o where o.customer.uuid = :uuid")
})
public class OrderEntity implements Serializable {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "uuid")
    @NotNull
    @Size(max = 200)
    private String uuid;
    
    @Column(name = "bill")
    private Double bill;
    
    @OneToOne
    @JoinColumn(name = "COUPON_ID")
    private Coupon coupon;
    
    @Column(name = "discount")
    private Double discount;
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getUuid() {
        return uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    public Double getBill() {
        return bill;
    }
    
    public void setBill(Double bill) {
        this.bill = bill;
    }
    
    public Coupon getCoupon() {
        return coupon;
    }
    
    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }
    
    public Double getDiscount() {
        return discount;
    }
    
    public void setDiscount(Double discount) {
        this.discount = discount;
    }
    
    public PaymentEntity getPayment() {
        return payment;
    }
    
    public void setPayment(PaymentEntity payment) {
        this.payment = payment;
    }
    
    public AddressEntity getAddress() {
        return address;
    }
    
    public void setAddress(AddressEntity address) {
        this.address = address;
    }
    
    public RestaurantEntity getRestaurant() {
        return restaurant;
    }
    
    public void setRestaurant(RestaurantEntity restaurant) {
        this.restaurant = restaurant;
    }
    
    public CustomerEntity getCustomer() {
        return customer;
    }
    
    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }
    
    
    @OneToOne
    @JoinColumn(name = "PAYMENT_ID")
    private PaymentEntity payment;
    
    @OneToOne
    @JoinColumn(name = "ADDRESS_ID")
    private AddressEntity address;
    
    @OneToOne
    @JoinColumn(name = "RESTAURANT_ID")
    private RestaurantEntity restaurant;
    
    @OneToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private CustomerEntity customer;
    
    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();
    
    public ZonedDateTime getDate() {
        return date;
    }
    
    public void setDate(ZonedDateTime date) {
        this.date = date;
    }
    
    @Column(name = "DATE")
    @NotNull
    private ZonedDateTime date;
}
