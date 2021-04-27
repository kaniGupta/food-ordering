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
@Table(name = "state")
@NamedQueries({
                      @NamedQuery(name = "stateById",
                                  query = "select s from StateEntity s where s.id = :id"),
                      @NamedQuery(name = "stateByUuid",
                                  query = "select s from StateEntity s where s.uuid = :uuid")
              })
public class StateEntity implements Serializable {
    private static final long serialVersionUID = -4881592165270352456L;
    
    public StateEntity(String uuid, String stateName) {
        this.uuid = uuid;
        this.stateName = stateName;
    }
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "state_name")
    @NotNull
    @Size(max = 30)
    private String stateName;
    
    public StateEntity() {
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

    public String getStateName() {
        return stateName;
    }

    public void setStateName(final String stateName) {
        this.stateName = stateName;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StateEntity)) {
            return false;
        }

        final StateEntity stateEntity = (StateEntity) o;

        if (!getId().equals(stateEntity.getId())) {
            return false;
        }
        if (!getUuid().equals(stateEntity.getUuid())) {
            return false;
        }
        return getStateName().equals(stateEntity.getStateName());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getUuid().hashCode();
        result = 31 * result + getStateName().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "State{" +
               "id=" + id +
               ", uuid='" + uuid + '\'' +
               ", stateName='" + stateName + '\'' +
               '}';
    }
}
