package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.dao.StateDao;
import com.upgrad.FoodOrderingApp.service.entity.Address;
import com.upgrad.FoodOrderingApp.service.entity.Customer;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuth;
import com.upgrad.FoodOrderingApp.service.entity.State;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import java.time.ZonedDateTime;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressBusinessService {
    
    private final AddressDao addressDao;
    private final CustomerDao customerDao;
    private final StateDao stateDao;
    
    @Autowired
    public AddressBusinessService(AddressDao addressDao,CustomerDao customerDao,StateDao stateDao) {
        this.addressDao = addressDao;
        this.customerDao = customerDao;
        this.stateDao = stateDao;
    }
    
    public Address save(final String accessToken,final Address address,final String stateId)
        throws AuthorizationFailedException, SaveAddressException, AddressNotFoundException {
        final CustomerAuth customerAuth = customerDao.getCustomerAuthToken(accessToken);
        if (customerAuth == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in");
        }
        final ZonedDateTime now = ZonedDateTime.now();
        if (customerAuth.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }
        if (customerAuth.getExpiresAt().compareTo(now) <= 0) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }
        if(!validateAddressRequest(address,stateId)) {
            throw new SaveAddressException("SAR-001","No field can be empty");
        }
        if(!validatePinCode(address.getPincode())) {
            throw new SaveAddressException("SAR-002","Invalid pincode");
        }
        State state = stateDao.getStateByUuid(stateId);
        if(state == null) {
            throw new AddressNotFoundException("ANF-002","No state by this id");
        }
        Customer customer = customerAuth.getCustomer();
        address.setState(state);
        address.setActive(1);
        if(!address.getCustomers().contains(customer)) {
            address.getCustomers().add(customer);
        }
        return addressDao.createAddress(address);
    }
    
    
    public List<Address> getAllAddressesOfCustomer(final String accessToken)
        throws AuthorizationFailedException, SaveAddressException, AddressNotFoundException {
        final CustomerAuth customerAuth = customerDao.getCustomerAuthToken(accessToken);
        if (customerAuth == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in");
        }
        final ZonedDateTime now = ZonedDateTime.now();
        if (customerAuth.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002",
                "Customer is logged out. Log in again to access this endpoint.");
        }
        if (customerAuth.getExpiresAt().compareTo(now) <= 0) {
            throw new AuthorizationFailedException("ATHR-003",
                "Your session is expired. Log in again to access this endpoint.");
        }
       Customer customer = customerAuth.getCustomer();
       return customer.getAddresses();
    }
    
    
    private boolean validateAddressRequest(Address address,String stateId) {
        if (address == null) {
            return false;
        } else if (address.getPincode() == null || StringUtils.isEmpty(address.getPincode())) {
            return false;
        } else if (address.getLocality() == null || StringUtils.isEmpty(address.getLocality())) {
            return false;
        } else if (address.getCity() == null || StringUtils.isEmpty(address.getCity())) {
            return false;
        } else if (address.getFlatBuilNumber() == null || StringUtils.isEmpty(
            address.getFlatBuilNumber())) {
            return false;
        } else if (stateId == null || StringUtils.isEmpty(stateId)) {
            return false;
        } else {
            return true;
        }
    }
    
    private boolean validatePinCode(String pinCode) {
        return pinCode.matches("\\d{6}");
    }
    
    
}
