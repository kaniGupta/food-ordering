package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.AddressList;
import com.upgrad.FoodOrderingApp.api.model.AddressListResponse;
import com.upgrad.FoodOrderingApp.api.model.AddressListState;
import com.upgrad.FoodOrderingApp.api.model.DeleteAddressResponse;
import com.upgrad.FoodOrderingApp.api.model.SaveAddressRequest;
import com.upgrad.FoodOrderingApp.api.model.SaveAddressResponse;
import com.upgrad.FoodOrderingApp.api.model.StatesList;
import com.upgrad.FoodOrderingApp.api.model.StatesListResponse;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import javax.swing.plaf.nimbus.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddressController {
    
    private final AddressService addressService;
    
    private final CustomerService customerService;
    
    @Autowired
    public AddressController(
        AddressService addressService,CustomerService customerService) {
        this.addressService = addressService;
        this.customerService = customerService;
    }
    
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Address successfully created"),
    })
    @RequestMapping(method = RequestMethod.POST, path="/address",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CrossOrigin
    public ResponseEntity<SaveAddressResponse> saveAddress(@RequestHeader final String authorization,
        @RequestBody(required = false) final SaveAddressRequest saveAddressRequest)
        throws AuthorizationFailedException, SaveAddressException, AddressNotFoundException {
        String accessToken=authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        StateEntity stateEntity = addressService.getStateByUUID(saveAddressRequest.getStateUuid());
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setCity(saveAddressRequest.getCity());
        addressEntity.setLocality(saveAddressRequest.getLocality());
        addressEntity.setFlatBuilNumber(saveAddressRequest.getFlatBuildingName());
        addressEntity.setPincode(saveAddressRequest.getPincode());
        addressEntity.setUuid(UUID.randomUUID().toString());
        addressEntity.setState(stateEntity);
        AddressEntity createdAddressEntity = addressService.saveAddress(addressEntity,customerEntity);
        SaveAddressResponse response = new SaveAddressResponse().id(createdAddressEntity.getUuid().toString()).status("ADDRESS SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SaveAddressResponse>(response, HttpStatus.CREATED);
    }
    
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Address successfully created"),
    })
    @RequestMapping(method = RequestMethod.GET,path="/address/customer",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CrossOrigin
    public ResponseEntity<AddressListResponse> getAllAddresses(@RequestHeader final String authorization)
        throws AuthorizationFailedException {
        String accessToken=authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        List<AddressEntity> addressEntities = addressService.getAllAddress(customerEntity);
        AddressListResponse response = new AddressListResponse();
        if(addressEntities == null || CollectionUtils.isEmpty(addressEntities)) {
            response.addresses(null);
        } else {
            response = converListOfAddress(addressEntities);
        }
        return new ResponseEntity<AddressListResponse>(response, HttpStatus.OK);
    }
    
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Address successfully deleted"),
    })
    @RequestMapping(method = RequestMethod.DELETE,path="/address/{address_id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CrossOrigin
    public ResponseEntity<DeleteAddressResponse> deleteAddress(@RequestHeader final String authorization,@PathVariable final String address_id)
        throws AuthorizationFailedException, AddressNotFoundException {
        String accessToken=authorization.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        AddressEntity addressEntity = addressService.getAddressByUUID(address_id,customerEntity);
        if(!addressEntity.getCustomers().contains(customerEntity)) {
            throw new AuthorizationFailedException("ATHR-004",
                "You are not authorized to view/update/delete any one else's address");
        }
        AddressEntity deletedAddressEntity = addressService.deleteAddress(addressEntity);
        DeleteAddressResponse response = new DeleteAddressResponse().id(UUID.fromString(address_id)).status("ADDRESS DELETED SUCCESSFULLY");
        return new ResponseEntity<DeleteAddressResponse>(response, HttpStatus.OK);
    }
    
    
    @RequestMapping(method = RequestMethod.GET,path="/states",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CrossOrigin
    public ResponseEntity<StatesListResponse> getAllStates() {
        List<StateEntity> stateEntities = addressService.getAllStates();
        return new ResponseEntity<StatesListResponse>(convertStates(stateEntities), HttpStatus.OK);
    }
    
    
    private AddressListResponse converListOfAddress(List<AddressEntity> addressEntities) {
        List<AddressList> addressLists = new ArrayList<>();
        AddressEntity addressEntity;
        AddressList al;
        //This ensures that the most recent address added will be displayed first
        Collections.sort(addressEntities, new Comparator<AddressEntity>() {
            @Override
            public int compare(AddressEntity o1, AddressEntity o2) {
                return o2.getId() - o1.getId();
            }
        });
        for(int i=0;i< addressEntities.size();i++) {
            addressEntity = addressEntities.get(i);
            al = new AddressList();
            al.city(addressEntity.getCity());
            al.flatBuildingName(addressEntity.getFlatBuilNumber());
            al.locality(addressEntity.getLocality());
            al.pincode(addressEntity.getPincode());
            al.id(UUID.fromString(addressEntity.getUuid()));
            AddressListState als = new AddressListState();
            als.id(UUID.fromString(addressEntity.getState().getUuid()));
            als.setStateName(addressEntity.getState().getStateName());
            al.state(als);
            addressLists.add(al);
        }
        AddressListResponse response = new AddressListResponse();
        response.setAddresses(addressLists);
        return response;
    }
    
    private StatesListResponse convertStates(List<StateEntity> stateEntities){
        List<StatesList> list = new ArrayList<>();
        StatesListResponse response = new StatesListResponse();
        if(CollectionUtils.isEmpty(stateEntities)) {
            response.states(null);
        } else {
            for (StateEntity s : stateEntities) {
                StatesList listEntry = new StatesList();
                listEntry.id(UUID.fromString(s.getUuid()));
                listEntry.stateName(s.getStateName());
                list.add(listEntry);
            }
            response.states(list);
        }
        return response;
    }
    
    
    
    
}
    

