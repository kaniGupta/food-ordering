package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerService {

    private static final String EMAIL_FORMAT = "^[A-Za-z0-9]+@(.+)$";
    private final CustomerDao customerDao;
    private final CustomerAuthDao customerAuthDao;
    private final PasswordCryptographyProvider passwordCryptographyProvider;

    @Autowired
    public CustomerService(final CustomerDao customerDao, final CustomerAuthDao customerAuthDao, final PasswordCryptographyProvider passwordCryptographyProvider) {
        this.customerDao = customerDao;
        this.customerAuthDao = customerAuthDao;
        this.passwordCryptographyProvider = passwordCryptographyProvider;
    }

    /**
     * Creates a customer
     *
     * @param customerEntity
     * @return the created customer
     * @throws SignUpRestrictedException
     */
    @Transactional
    public CustomerEntity saveCustomer(final CustomerEntity customerEntity) throws SignUpRestrictedException {
        final String password = customerEntity.getPassword();
        final CustomerEntity existingCustomerEntity;
        existingCustomerEntity = customerDao.getCustomerByContactNumber(customerEntity.getContactNumber());
        if (existingCustomerEntity != null) {
            throw new SignUpRestrictedException("SGR-001",
                                                "This contact number is already registered! Try other contact number.");
        }
        if (!validateEmail(customerEntity.getEmail())) {
            throw new SignUpRestrictedException("SGR-002",
                                                "Invalid email-id format!");
        }
        if (!validateContactNumber(customerEntity.getContactNumber())) {
            throw new SignUpRestrictedException("SGR-003",
                                                "Invalid contact number!");
        }
        
        if (!validatePassword(customerEntity.getPassword())) {
            throw new SignUpRestrictedException("SGR-004",
                                                "Weak password!");
        }
        //Encrypt the password
        final String[] encryptedText = passwordCryptographyProvider.encrypt(customerEntity.getPassword());
        customerEntity.setSalt(encryptedText[0]);
        customerEntity.setPassword(encryptedText[1]);
        return customerDao.createCustomer(customerEntity);

    }
    
    /**
     * Login a customer
     *
     * @param contactNumber
     * @param password
     * @return
     * @throws AuthenticationFailedException
     */
    @Transactional
    public CustomerAuthEntity authenticate(final String contactNumber, final String password)
            throws AuthenticationFailedException {
        final CustomerEntity customerEntity = customerDao.getCustomerByContactNumber(contactNumber);
        if (customerEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
        }
        final String encryptedPassword = PasswordCryptographyProvider
                .encrypt(password, customerEntity.getSalt());
        if (encryptedPassword.equals(customerEntity.getPassword())) {
            final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            final CustomerAuthEntity customerAuthToken = new CustomerAuthEntity();
            customerAuthToken.setCustomer(customerEntity);
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);
            customerAuthToken.setAccessToken(
                    jwtTokenProvider.generateToken(customerEntity.getUuid(), now, expiresAt));
            customerAuthToken.setLoginAt(now);
            customerAuthToken.setExpiresAt(expiresAt);
            customerAuthToken.setUuid(UUID.randomUUID().toString());
            customerDao.createAuthToken(customerAuthToken);
            return customerAuthToken;
        } else {
            throw new AuthenticationFailedException("ATH-002", "Invalid Credentials");
        }

    }
    
    
    /**
     * Logout a customer
     *
     * @param accessToken
     * @return
     * @throws AuthorizationFailedException
     */
    @Transactional
    public CustomerAuthEntity logout(final String accessToken) throws AuthorizationFailedException {
        final CustomerAuthEntity customerAuth = customerDao.getCustomerAuthToken(accessToken);
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
        customerAuth.setLogoutAt(now);
        customerDao.updateCustomerAuthEntity(customerAuth);
        return customerAuth;
    }
    
    
    /**
     * Update a customer
     *
     * @param updatedCustomerEntity
     * @return
     * @throws AuthorizationFailedException
     * @throws UpdateCustomerException
     */
    @Transactional
    public CustomerEntity updateCustomer(final CustomerEntity updatedCustomerEntity) throws AuthorizationFailedException, UpdateCustomerException {
        customerDao.updateCustomer(updatedCustomerEntity);
        return updatedCustomerEntity;
    }
    
    /**
     * Update password of a customer
     *
     * @param oldPassword
     * @param newPassword
     * @param customerEntity
     * @return
     * @throws AuthorizationFailedException
     * @throws UpdateCustomerException
     */
    @Transactional
    public CustomerEntity updateCustomerPassword(final String oldPassword, final String newPassword,CustomerEntity customerEntity) throws AuthorizationFailedException, UpdateCustomerException{
        if (oldPassword == null || StringUtils.isEmpty(oldPassword) || newPassword == null || StringUtils.isEmpty(newPassword)) {
            throw new UpdateCustomerException("UCR-003", "No field should be empty");
        }
        final String[] encryptedText = passwordCryptographyProvider.encrypt(oldPassword);
        // If encrypted password stored in database and encrypted password from request do not match , throw exception
        if (!StringUtils.equals(encryptedText[1], oldPassword)) {
            throw new UpdateCustomerException("UCR-004", "Incorrect old password");
        }
        if (!validatePassword(newPassword)) {
            throw new UpdateCustomerException("UCR-001", "Weak password!");
        }
        final String[] encryptedTextForNewPassword = passwordCryptographyProvider.encrypt(newPassword);
        customerEntity.setPassword(encryptedTextForNewPassword[1]);
        customerEntity.setSalt(encryptedTextForNewPassword[0]);
        customerDao.updateCustomer(customerEntity);
        return customerEntity;
    }
    
    
    /**
     *
     * @param accessToken
     * @return
     * @throws AuthorizationFailedException
     */
    public CustomerEntity getCustomer(String accessToken) throws AuthorizationFailedException {
        final CustomerAuthEntity customerAuth = customerDao.getCustomerAuthToken(accessToken);
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
        final CustomerEntity customerEntity = customerAuth.getCustomer();
        return customerEntity;
    }
    
    
    /**
     * Validates an email
     * @param email
     * @return
     */
    private boolean validateEmail(final String email) {
        final Pattern pattern = Pattern.compile(EMAIL_FORMAT);
        final Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
    /**
     * Validates a contact number
     * @param contactNumber
     * @return
     */
    private boolean validateContactNumber(final String contactNumber) {
        return contactNumber.matches("\\d{10}");
    }
    
    /**
     * Validates a password
     * @param password
     * @return
     */
    private boolean validatePassword(final String password) {
        return password.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}");
    }
    

}

