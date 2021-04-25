package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.AddressList;
import com.upgrad.FoodOrderingApp.api.model.AddressListResponse;
import com.upgrad.FoodOrderingApp.api.model.AddressListState;
import com.upgrad.FoodOrderingApp.api.model.SaveAddressRequest;
import com.upgrad.FoodOrderingApp.api.model.SaveAddressResponse;
import com.upgrad.FoodOrderingApp.api.model.UpdateCustomerResponse;
import com.upgrad.FoodOrderingApp.service.businness.AddressBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.Address;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
public class AddressController {
    
    private final AddressBusinessService addressBusinessService;
    
    @Autowired
    public AddressController(
        AddressBusinessService addressBusinessService) {
        this.addressBusinessService = addressBusinessService;
    }
    
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Address successfully created"),
    })
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CrossOrigin
    public ResponseEntity<SaveAddressResponse> saveAddress(@RequestHeader final String authorization,
        @RequestBody(required = false) final SaveAddressRequest saveAddressRequest)
        throws AuthorizationFailedException, SaveAddressException, AddressNotFoundException {
        Address address = new Address();
        address.setCity(saveAddressRequest.getCity());
        address.setLocality(saveAddressRequest.getLocality());
        address.setFlatBuilNumber(saveAddressRequest.getFlatBuildingName());
        address.setPincode(saveAddressRequest.getPincode());
        address.setUuid(UUID.randomUUID().toString());
        Address createdAddress = addressBusinessService.save(authorization,address,saveAddressRequest.getStateUuid());
        SaveAddressResponse response = new SaveAddressResponse().id(createdAddress.getUuid().toString()).status("ADDRESS SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SaveAddressResponse>(response, HttpStatus.OK);
    }
    
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Address successfully created"),
    })
    @RequestMapping(method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CrossOrigin
    public ResponseEntity<AddressListResponse> getAllAddresses(@RequestHeader final String authorization)
        throws AuthorizationFailedException, SaveAddressException, AddressNotFoundException {
        List<Address> addresses = addressBusinessService.getAllAddressesOfCustomer(authorization);
        
        AddressListResponse response = new AddressListResponse();
        if(addresses == null || CollectionUtils.isEmpty(addresses)) {
            return new ResponseEntity<AddressListResponse>(response, HttpStatus.OK);
        } else {
            response = converListOfAddress(addresses);
        }
        return new ResponseEntity<AddressListResponse>(response, HttpStatus.OK);
    }
    
    private AddressListResponse converListOfAddress(List<Address> addresses) {
        List<AddressList> addressLists = new ArrayList<>();
        Address address;
        AddressList al;
        //This ensures that the most recent address added will be displayed first
        Collections.sort(addresses, new Comparator<Address>() {
            @Override
            public int compare(Address o1, Address o2) {
                return o2.getId() - o1.getId();
            }
        });
        for(int i=0;i<addresses.size();i++) {
            address = addresses.get(i);
            al = new AddressList();
            al.city(address.getCity());
            al.flatBuildingName(address.getFlatBuilNumber());
            al.locality(address.getLocality());
            al.pincode(address.getPincode());
            al.id(UUID.fromString(address.getUuid()));
            AddressListState als = new AddressListState();
            als.id(UUID.fromString(address.getState().getUuid()));
            als.setStateName(address.getState().getStateName());
            al.state(als);
            addressLists.add(al);
        }
        AddressListResponse response = new AddressListResponse();
        response.setAddresses(addressLists);
        return response;
    }
    
}
    
