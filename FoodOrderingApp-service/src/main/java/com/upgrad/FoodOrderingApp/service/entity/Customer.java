package com.upgrad.FoodOrderingApp.service.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "CUSTOMER")
@NamedQueries(
        {
                @NamedQuery(name = "customerByUuid", query = "select c from Customer c where c.uuid = :uuid"),
                @NamedQuery(name = "customerByEmail", query = "select c from Customer c where c.email =:email"),
                @NamedQuery(name = "customerByContactNumber",
                            query = "select c from Customer c where c.contactNumber =:contactNumber"),
        }
)
public class Customer implements Serializable {

    private static final long serialVersionUID = -240597140638746842L;
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "FIRSTNAME")
    @NotNull
    @Size(max = 30)
    private String firstName;

    @Column(name = "LASTNAME")
    @NotNull
    @Size(max = 30)
    private String lastName;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(final String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(final String salt) {
        this.salt = salt;
    }

    @Column(name = "EMAIL", unique = true)
    @NotNull
    @Email
    @Size(max = 50)
    private String email;

    @Column(name = "CONTACT_NUMBER")
    @Size(max = 30)
    private String contactNumber;

    @Column(name = "PASSWORD")
    @NotNull
    @Size(max = 255)
    private String password;

    @Column(name = "SALT")
    @NotNull
    @Size(max = 200)
    private String salt;
    
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "CUSTOMER_ADDRESS",
        joinColumns = @JoinColumn(name = "CUSTOMER_ID"),
        inverseJoinColumns = @JoinColumn(name = "ADDRESS_ID"))
    private List<Address> addresses = new ArrayList<>();
    
    public List<Address> getAddresses() {
        return addresses;
    }
    
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        final Customer that = (Customer) o;
        return id.equals(that.id) && uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid);
    }
}
