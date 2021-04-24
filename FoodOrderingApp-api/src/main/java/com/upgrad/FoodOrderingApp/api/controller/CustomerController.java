package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.LoginResponse;
import com.upgrad.FoodOrderingApp.api.model.LogoutResponse;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerRequest;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerResponse;
import com.upgrad.FoodOrderingApp.api.model.UpdateCustomerRequest;
import com.upgrad.FoodOrderingApp.api.model.UpdateCustomerResponse;
import com.upgrad.FoodOrderingApp.api.model.UpdatePasswordRequest;
import com.upgrad.FoodOrderingApp.api.model.UpdatePasswordResponse;
import com.upgrad.FoodOrderingApp.service.businness.CustomerBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
@RequestMapping("/customer")
public class CustomerController {
    
    private final CustomerBusinessService customerBusinessService;
    
    @Autowired
    public CustomerController(final CustomerBusinessService customerBusinessService) {
        this.customerBusinessService = customerBusinessService;
    }
    
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Customer Successfully registered"),
    })
    @RequestMapping(method = RequestMethod.POST, path = "/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CrossOrigin
    public ResponseEntity<SignupCustomerResponse> signup(
        @RequestBody(required = false) final SignupCustomerRequest signupCustomerRequest)
        throws SignUpRestrictedException {
        
        final CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setUuid(UUID.randomUUID().toString());
        customerEntity.setFirstName(signupCustomerRequest.getFirstName());
        customerEntity.setLastName(signupCustomerRequest.getLastName());
        customerEntity.setEmail(signupCustomerRequest.getEmailAddress());
        customerEntity.setPassword(signupCustomerRequest.getPassword());
        customerEntity.setContactNumber(signupCustomerRequest.getContactNumber());
        final CustomerEntity createdCustomerEntity = customerBusinessService
                                                         .createCustomer(customerEntity);
        SignupCustomerResponse customerResponse = new SignupCustomerResponse()
                                                      .id(createdCustomerEntity.getUuid())
                                                      .status("CUSTOMER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupCustomerResponse>(customerResponse, HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.POST, path = "/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> login(
        @RequestHeader("authorization") final String authorization)
        throws AuthenticationFailedException {
        CustomerAuthEntity customerAuthToken = customerBusinessService
                                                   .login(authorization);
        CustomerEntity customer = customerAuthToken.getCustomer();
        LoginResponse loginResponse = new LoginResponse().id(customer.getUuid())
                                          .contactNumber(customer.getContactNumber())
                                          .firstName(customer.getFirstName())
                                          .lastName(customer.getLastName())
                                          .emailAddress(customer.getEmail())
                                          .message("LOGGED IN SUCCESSFULLY");
        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token", customerAuthToken.getAccessToken());
        List<String> header = new ArrayList<>();
        header.add("access-token");
        headers.setAccessControlExposeHeaders(header);
        return new ResponseEntity<LoginResponse>(loginResponse, headers, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, path = "/logout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LogoutResponse> logout(
        @RequestHeader("authorization") final String authorization)
        throws AuthorizationFailedException {
        CustomerAuthEntity customerAuthToken = customerBusinessService.logout(authorization);
        CustomerEntity customer = customerAuthToken.getCustomer();
        LogoutResponse logoutResponse = new LogoutResponse().id(customer.getUuid())
                                            .message("LOGGED OUT SUCCESSFULLY");
        return new ResponseEntity<LogoutResponse>(logoutResponse, HttpStatus.OK);
    }
    
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "CUSTOMER DETAILS UPDATED SUCCESSFULLY"),
    })
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdateCustomerResponse> update(@RequestHeader("authorization") final String authorization,
        @RequestBody(required = false) final UpdateCustomerRequest updateCustomerRequest)
        throws SignUpRestrictedException, AuthorizationFailedException, UpdateCustomerException {
        final CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setUuid(UUID.randomUUID().toString());
        customerEntity.setFirstName(updateCustomerRequest.getFirstName());
        customerEntity.setLastName(updateCustomerRequest.getLastName());
        final CustomerEntity updatedCustomerEntity = customerBusinessService.update(authorization, customerEntity);
        UpdateCustomerResponse customerResponse = new UpdateCustomerResponse().id(updatedCustomerEntity.getUuid()).firstName(updatedCustomerEntity.getFirstName()).
                                                                                                                                                                      lastName(updatedCustomerEntity.getLastName())
                                                      .status("CUSTOMER DETAILS UPDATED SUCCESSFULLY");
        return new ResponseEntity<UpdateCustomerResponse>(customerResponse, HttpStatus.OK);
    }
    
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "CUSTOMER PASSWORD UPDATED SUCCESSFULLY"),
    })
    @RequestMapping(method = RequestMethod.PUT, path = "/password", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdatePasswordResponse> changePassword(@RequestHeader("authorization") final String authorization,
        @RequestBody(required = false) final UpdatePasswordRequest updatePasswordRequest)
        throws SignUpRestrictedException, AuthorizationFailedException, UpdateCustomerException {
        final CustomerEntity updatedCustomerEntity = customerBusinessService.updatePassword(authorization, updatePasswordRequest.getOldPassword(), updatePasswordRequest.getNewPassword());
        UpdatePasswordResponse passwordResponse = new UpdatePasswordResponse().id(updatedCustomerEntity.getUuid()).status("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");
        return new ResponseEntity<UpdatePasswordResponse>(passwordResponse, HttpStatus.OK);
    }
    
}
