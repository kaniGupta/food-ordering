package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.LoginResponse;
import com.upgrad.FoodOrderingApp.api.model.LogoutResponse;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerRequest;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerResponse;
import com.upgrad.FoodOrderingApp.api.model.UpdateCustomerRequest;
import com.upgrad.FoodOrderingApp.api.model.UpdateCustomerResponse;
import com.upgrad.FoodOrderingApp.api.model.UpdatePasswordRequest;
import com.upgrad.FoodOrderingApp.api.model.UpdatePasswordResponse;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuth;
import com.upgrad.FoodOrderingApp.service.entity.Customer;
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
    
    private final CustomerService customerService;
    
    @Autowired
    public CustomerController(final CustomerService customerService) {
        this.customerService = customerService;
    }
    
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Customer Successfully registered"),
    })
    @RequestMapping(method = RequestMethod.POST, path = "/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CrossOrigin
    public ResponseEntity<SignupCustomerResponse> signup(
        @RequestBody(required = false) final SignupCustomerRequest signupCustomerRequest)
        throws SignUpRestrictedException {
        
        final Customer customer = new Customer();
        customer.setUuid(UUID.randomUUID().toString());
        customer.setFirstName(signupCustomerRequest.getFirstName());
        customer.setLastName(signupCustomerRequest.getLastName());
        customer.setEmail(signupCustomerRequest.getEmailAddress());
        customer.setPassword(signupCustomerRequest.getPassword());
        customer.setContactNumber(signupCustomerRequest.getContactNumber());
        final Customer createdCustomer = customerService
                                                         .createCustomer(customer);
        SignupCustomerResponse customerResponse = new SignupCustomerResponse()
                                                      .id(createdCustomer.getUuid())
                                                      .status("CUSTOMER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupCustomerResponse>(customerResponse, HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.POST, path = "/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> login(
        @RequestHeader("authorization") final String authorization)
        throws AuthenticationFailedException {
        CustomerAuth customerAuthToken = customerService
                                                   .login(authorization);
        Customer customer = customerAuthToken.getCustomer();
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
        CustomerAuth customerAuthToken = customerService.logout(authorization);
        Customer customer = customerAuthToken.getCustomer();
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
        final Customer customer = new Customer();
        customer.setUuid(UUID.randomUUID().toString());
        customer.setFirstName(updateCustomerRequest.getFirstName());
        customer.setLastName(updateCustomerRequest.getLastName());
        final Customer updatedCustomer = customerService.update(authorization, customer);
        UpdateCustomerResponse customerResponse = new UpdateCustomerResponse().id(updatedCustomer.getUuid()).firstName(
            updatedCustomer.getFirstName()).
                                                                                                                                                                      lastName(
                                                                                                                                                                          updatedCustomer
                                                                                                                                                                              .getLastName())
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
        final Customer updatedCustomer = customerService
                                             .updatePassword(authorization, updatePasswordRequest.getOldPassword(), updatePasswordRequest.getNewPassword());
        UpdatePasswordResponse passwordResponse = new UpdatePasswordResponse().id(updatedCustomer.getUuid()).status("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");
        return new ResponseEntity<UpdatePasswordResponse>(passwordResponse, HttpStatus.OK);
    }
    
}
