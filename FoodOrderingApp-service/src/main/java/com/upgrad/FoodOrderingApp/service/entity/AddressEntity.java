package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "address")
@NamedQueries({
                      @NamedQuery(name = "addressById",
                                  query = "select a from AddressEntity a where a.id = :id"),
                      @NamedQuery(name = "addressByUuid",
                                  query = "select a from AddressEntity a where a.uuid = :uuid"),
              })
public class AddressEntity implements Serializable {

    private static final long serialVersionUID = -3318717135185330458L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "flat_buil_number")
    @Size(max = 255)
    private String flatBuilNumber;

    @Column(name = "locality")
    @Size(max = 255)
    private String locality;

    @Column(name = "city")
    @Size(max = 30)
    private String city;

    @Column(name = "pincode")
    @Size(max = 30)
    private String pincode;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private StateEntity stateEntity;
    @Column(name = "active")
    private Integer active;
    @ManyToMany(targetEntity = CustomerEntity.class, cascade = {
            CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
    })
    @JoinTable(name = "CUSTOMER_ADDRESS",
               joinColumns = @JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ID"),
               inverseJoinColumns = @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID"))
    private List<CustomerEntity> customers = new ArrayList<>();

    public List<CustomerEntity> getCustomers() {
        return customers;
    }

    public void setCustomers(
            final List<CustomerEntity> customers) {
        this.customers = customers;
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

    public String getFlatBuilNumber() {
        return flatBuilNumber;
    }

    public void setFlatBuilNumber(final String flatBuilNumber) {
        this.flatBuilNumber = flatBuilNumber;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(final String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(final String pincode) {
        this.pincode = pincode;
    }

    public StateEntity getState() {
        return stateEntity;
    }

    public void setState(final StateEntity stateEntity) {
        this.stateEntity = stateEntity;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(final Integer active) {
        this.active = active;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AddressEntity)) {
            return false;
        }

        final AddressEntity addressEntity = (AddressEntity) o;

        if (getId() != null ? !getId().equals(addressEntity.getId()) : addressEntity.getId() != null) {
            return false;
        }
        if (getUuid() != null ? !getUuid().equals(addressEntity.getUuid()) : addressEntity.getUuid() != null) {
            return false;
        }
        if (getFlatBuilNumber() != null ? !getFlatBuilNumber().equals(addressEntity.getFlatBuilNumber()) :
            addressEntity
                    .getFlatBuilNumber()
            != null) {
            return false;
        }
        if (getLocality() != null ? !getLocality().equals(addressEntity.getLocality())
                                  : addressEntity.getLocality() != null) {
            return false;
        }
        if (getCity() != null ? !getCity().equals(addressEntity.getCity()) : addressEntity.getCity() != null) {
            return false;
        }
        if (getPincode() != null ? !getPincode().equals(addressEntity.getPincode())
                                 : addressEntity.getPincode() != null) {
            return false;
        }
        if (getState() != null ? !getState().equals(addressEntity.getState())
                               : addressEntity.getState() != null) {
            return false;
        }
        return getActive() != null ? getActive().equals(addressEntity.getActive())
                                   : addressEntity.getActive() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getUuid() != null ? getUuid().hashCode() : 0);
        result = 31 * result + (getFlatBuilNumber() != null ? getFlatBuilNumber().hashCode() : 0);
        result = 31 * result + (getLocality() != null ? getLocality().hashCode() : 0);
        result = 31 * result + (getCity() != null ? getCity().hashCode() : 0);
        result = 31 * result + (getPincode() != null ? getPincode().hashCode() : 0);
        result = 31 * result + (getState() != null ? getState().hashCode() : 0);
        result = 31 * result + (getActive() != null ? getActive().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Address{" +
               "id=" + id +
               ", uuid='" + uuid + '\'' +
               ", flatBuilNumber='" + flatBuilNumber + '\'' +
               ", locality='" + locality + '\'' +
               ", city='" + city + '\'' +
               ", pincode='" + pincode + '\'' +
               ", state=" + stateEntity +
               ", active=" + active +
               '}';
    }
}
