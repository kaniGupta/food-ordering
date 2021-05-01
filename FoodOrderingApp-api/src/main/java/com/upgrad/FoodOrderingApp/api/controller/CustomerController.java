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
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
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
        
        final CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setUuid(UUID.randomUUID().toString());
        customerEntity.setFirstName(signupCustomerRequest.getFirstName());
        customerEntity.setLastName(signupCustomerRequest.getLastName());
        customerEntity.setEmail(signupCustomerRequest.getEmailAddress());
        customerEntity.setPassword(signupCustomerRequest.getPassword());
        customerEntity.setContactNumber(signupCustomerRequest.getContactNumber());
        if (!validateCustomer(customerEntity)) {
            throw new SignUpRestrictedException("SGR-005",
                "Except last name all fields should be filled");
        }
        final CustomerEntity createdCustomerEntity = customerService
                                                         .saveCustomer(customerEntity);
        SignupCustomerResponse customerResponse = new SignupCustomerResponse()
                                                      .id(createdCustomerEntity.getUuid())
                                                      .status("CUSTOMER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupCustomerResponse>(customerResponse, HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.POST, path = "/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> login(
        @RequestHeader("authorization") final String authorization)
        throws AuthenticationFailedException {
        final String base64EncodedCredentials = authorization.split("Basic ")[1];
        if (base64EncodedCredentials == null || StringUtils.isEmpty(base64EncodedCredentials)) {
            throw new AuthenticationFailedException("ATH-003",
                "Incorrect format of decoded customer name and password");
        }
        final byte[] decode = Base64.getDecoder().decode(base64EncodedCredentials);
        final String decodedText = new String(decode);
        final String[] decodedArray = decodedText.split(":");
        if(decodedArray == null || decodedArray.length<2) {
            throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");
        }
        final String contactNumber = decodedArray[0];
        final String password = decodedArray[1];
        if (password == null || StringUtils.isEmpty(password) || contactNumber == null || StringUtils.isEmpty(contactNumber)) {
            throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");
        }
        CustomerAuthEntity customerAuthToken = customerService
                                                   .authenticate(contactNumber, password);
        CustomerEntity customerEntity = customerAuthToken.getCustomer();
        LoginResponse loginResponse = new LoginResponse().id(customerEntity.getUuid())
                                          .contactNumber(customerEntity.getContactNumber())
                                          .firstName(customerEntity.getFirstName())
                                          .lastName(customerEntity.getLastName())
                                          .emailAddress(customerEntity.getEmail())
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
        String accessToken = authorization.split("Bearer ")[1];
        CustomerAuthEntity customerAuthToken = customerService.logout(accessToken);
        CustomerEntity customerEntity = customerAuthToken.getCustomer();
        LogoutResponse logoutResponse = new LogoutResponse().id(customerEntity.getUuid())
                                            .message("LOGGED OUT SUCCESSFULLY");
        return new ResponseEntity<LogoutResponse>(logoutResponse, HttpStatus.OK);
    }
    
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "CUSTOMER DETAILS UPDATED SUCCESSFULLY"),
    })
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdateCustomerResponse> update(
        @RequestHeader("authorization") final String authorization,
        @RequestBody(required = false) final UpdateCustomerRequest updateCustomerRequest)
        throws SignUpRestrictedException, AuthorizationFailedException, UpdateCustomerException {
        String accessToken=authorization.split("Bearer ")[1];
        if(updateCustomerRequest != null && (updateCustomerRequest.getFirstName() == null || StringUtils.isEmpty(updateCustomerRequest.getFirstName()))) {
            throw new UpdateCustomerException("UCR-002","First name field should not be empty");
        }
        final CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        customerEntity.setFirstName(updateCustomerRequest.getFirstName());
        customerEntity.setLastName(updateCustomerRequest.getLastName());
        final CustomerEntity updatedCustomerEntity = customerService.updateCustomer(customerEntity);
        UpdateCustomerResponse customerResponse = new UpdateCustomerResponse().id(
            updatedCustomerEntity.getUuid()).firstName(
            updatedCustomerEntity.getFirstName()).
                                                     lastName(
                                                         updatedCustomerEntity
                                                             .getLastName())
                                                      .status(
                                                          "CUSTOMER DETAILS UPDATED SUCCESSFULLY");
        return new ResponseEntity<UpdateCustomerResponse>(customerResponse, HttpStatus.OK);
    }
    
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "CUSTOMER PASSWORD UPDATED SUCCESSFULLY"),
    })
    @RequestMapping(method = RequestMethod.PUT, path = "/password", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdatePasswordResponse> changePassword(
        @RequestHeader("authorization") final String authorization,
        @RequestBody(required = false) final UpdatePasswordRequest updatePasswordRequest)
        throws AuthorizationFailedException, UpdateCustomerException {
        String accessToken = authorization.split("Bearer ")[1];
        String oldPassword = updatePasswordRequest.getOldPassword();
        String newPassword = updatePasswordRequest.getNewPassword();
        if (oldPassword == null || StringUtils.isEmpty(oldPassword) || newPassword == null || StringUtils.isEmpty(newPassword)) {
            throw new UpdateCustomerException("UCR-003", "No field should be empty");
        }
        final CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        final CustomerEntity updatedCustomerEntity = customerService
                                                         .updateCustomerPassword(
                                                             updatePasswordRequest.getOldPassword(),
                                                             updatePasswordRequest
                                                                 .getNewPassword(),customerEntity);
        UpdatePasswordResponse passwordResponse = new UpdatePasswordResponse().id(
            updatedCustomerEntity.getUuid()).status("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");
        return new ResponseEntity<UpdatePasswordResponse>(passwordResponse, HttpStatus.OK);
    }
    
    /**
     * Validates a customer entity object
     *
     * @param customerEntity
     * @return false when any field other than last name is empty
     */
    private boolean validateCustomer(final CustomerEntity customerEntity) {
        if (Objects.isNull(customerEntity.getFirstName()) || StringUtils.isEmpty(customerEntity.getFirstName())) {
            return false;
        } else if (Objects.isNull(customerEntity.getEmail()) || StringUtils.isEmpty(customerEntity.getEmail())) {
            return false;
        } else if (Objects.isNull(customerEntity.getPassword()) || StringUtils.isEmpty(
            customerEntity.getPassword())) {
            return false;
        } else {
            return !Objects.isNull(customerEntity.getContactNumber()) && !StringUtils.isEmpty(
                customerEntity.getContactNumber());
        }
    }
    
    
}
