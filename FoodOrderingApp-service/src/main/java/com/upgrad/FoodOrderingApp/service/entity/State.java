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
import java.util.UUID;

@Entity
@Table(name = "state")
@NamedQueries({
                      @NamedQuery(name = "stateById",
                                  query = "select s from State s where s.id = :id"),
    @NamedQuery(name = "stateByUuid",
        query = "select s from State s where s.uuid = :uuid"),
              })
public class State implements Serializable {
    private static final long serialVersionUID = -4881592165270352456L;

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
        if (!(o instanceof State)) {
            return false;
        }

        final State state = (State) o;

        if (!getId().equals(state.getId())) {
            return false;
        }
        if (!getUuid().equals(state.getUuid())) {
            return false;
        }
        return getStateName().equals(state.getStateName());
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
