package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class AddressDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public AddressEntity getAddressById(final Integer id) {
        try {
            return entityManager.createNamedQuery("addressById", AddressEntity.class)
                                .setParameter("id", id)
                                .getSingleResult();
        } catch (final NoResultException nre) {
            return null;
        }
    }

    @Transactional
    public AddressEntity getAddressByUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("addressByUuid", AddressEntity.class)
                                .setParameter("uuid", uuid)
                                .getSingleResult();
        } catch (final NoResultException nre) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity createAddress(AddressEntity addressEntity) {
        entityManager.persist(addressEntity);
        return addressEntity;
    }

    @Transactional
    public void deleteAddress(AddressEntity addressEntity) {
        entityManager.remove(addressEntity);
    }
}
