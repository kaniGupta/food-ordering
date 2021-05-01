package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    
    private final AddressDao addressDao;
    private final CustomerDao customerDao;
    private final StateDao stateDao;
    
    @Autowired
    public AddressService(AddressDao addressDao,CustomerDao customerDao,StateDao stateDao) {
        this.addressDao = addressDao;
        this.customerDao = customerDao;
        this.stateDao = stateDao;
    }
    
    public AddressEntity saveAddress(final AddressEntity addressEntity,final CustomerEntity customerEntity)
        throws SaveAddressException, AddressNotFoundException {
        if(!validateAddressRequest(addressEntity)) {
            throw new SaveAddressException("SAR-001","No field can be empty");
        }
        if(!validatePinCode(addressEntity.getPincode())) {
            throw new SaveAddressException("SAR-002","Invalid pincode");
        }
        if(addressEntity.getState() == null) {
            throw new AddressNotFoundException("ANF-002","No state by this id");
        }
        addressEntity.setActive(1);
        if(!addressEntity.getCustomers().contains(customerEntity)) {
            addressEntity.getCustomers().add(customerEntity);
        }
        return addressDao.createAddress(addressEntity);
    }
    
    
    public List<AddressEntity> getAllAddress(final CustomerEntity customerEntity) {
       return customerEntity.getAddresses();
    }
    
    public AddressEntity deleteAddress(final AddressEntity addressEntity) {
        addressDao.deleteAddress(addressEntity);
        return addressEntity;
    }
    
    
    private boolean validateAddressRequest(AddressEntity addressEntity) {
        if (addressEntity == null) {
            return false;
        } else if (addressEntity.getPincode() == null || StringUtils.isEmpty(addressEntity.getPincode())) {
            return false;
        } else if (addressEntity.getLocality() == null || StringUtils.isEmpty(addressEntity.getLocality())) {
            return false;
        } else if (addressEntity.getCity() == null || StringUtils.isEmpty(addressEntity.getCity())) {
            return false;
        } else if (addressEntity.getFlatBuilNumber() == null || StringUtils.isEmpty(
            addressEntity.getFlatBuilNumber())) {
            return false;
        } else {
            return true;
        }
    }
    
    private boolean validatePinCode(String pinCode) {
        return pinCode.matches("\\d{6}");
    }
    
    public List<StateEntity> getAllStates() {
        return stateDao.getAllStates();
    }
    
    public StateEntity getStateByUUID(String uuid) throws AddressNotFoundException {
       StateEntity stateEntity= stateDao.getStateByUuid(uuid);
       if(Objects.isNull(stateEntity)) {
           throw new AddressNotFoundException("ANF-002","No state by this id");
       }
       else {
           return stateEntity;
       }
    }
    
    public AddressEntity getAddressByUUID(String uuid,CustomerEntity customerEntity)
        throws AddressNotFoundException,AuthorizationFailedException {
        AddressEntity addressEntity = addressDao.getAddressByUuid(uuid);
        if(addressEntity == null) {
            throw new AddressNotFoundException("ANF-003","No address by this id");
        }
        AddressEntity result = null;
        for(AddressEntity customerAddress : customerEntity.getAddresses()) {
            if(addressEntity.equals(customerAddress)) {
                result = addressEntity;
                break;
            }
        }
        if(Objects.isNull(result)) {
            new AuthorizationFailedException("ATHR-004", "You are not authorized to view/update/delete any one else's address");
        }
        return result;
    }
    
    
}
