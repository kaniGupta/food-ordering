package com.upgrad.FoodOrderingApp.api.controller;

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
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    
}
    

