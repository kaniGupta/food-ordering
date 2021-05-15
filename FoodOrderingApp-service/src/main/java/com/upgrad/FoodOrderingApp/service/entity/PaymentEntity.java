package com.upgrad.FoodOrderingApp.service.entity;

import java.io.Serializable;
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

@Entity
@Table(name = "payment")
@NamedQueries(
    @NamedQuery(name = "paymentByUuid", query = "select p from PaymentEntity p where p.uuid = :uuid")
)
public class PaymentEntity implements Serializable {
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentEntity)) {
            return false;
        }
        PaymentEntity that = (PaymentEntity) o;
        return id.equals(that.id) && uuid.equals(that.uuid) && paymentName.equals(that.paymentName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, paymentName);
    }
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "uuid")
    @NotNull
    @Size(max = 200)
    private String uuid;
    
    @Column(name="payment_name")
    @NotNull
    @Size(max=255)
    private String paymentName;
    
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
    
    public String getPaymentName() {
        return paymentName;
    }
    
    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }
}
