package com.upgrad.FoodOrderingApp.service.entity;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "address")
@NamedQueries({
                      @NamedQuery(name = "addressById",
                                  query = "select a from Address a where a.id = :id"),
              })

public class Address implements Serializable {

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
    private State state;

    @Column(name = "active")
    private Integer active;
}
