package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuth;
import com.upgrad.FoodOrderingApp.service.entity.Customer;
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
public class CustomerBusinessService {

    private static final String EMAIL_FORMAT = "^[A-Za-z0-9]+@(.+)$";
    private final CustomerDao customerDao;
    private final CustomerAuthDao customerAuthDao;
    private final PasswordCryptographyProvider passwordCryptographyProvider;

    @Autowired
    public CustomerBusinessService(final CustomerDao customerDao, final CustomerAuthDao customerAuthDao, final PasswordCryptographyProvider passwordCryptographyProvider) {
        this.customerDao = customerDao;
        this.customerAuthDao = customerAuthDao;
        this.passwordCryptographyProvider = passwordCryptographyProvider;
    }

    /**
     * Creates a customer
     *
     * @param customer
     * @return the created customer
     * @throws SignUpRestrictedException
     */
    @Transactional
    public Customer createCustomer(final Customer customer) throws SignUpRestrictedException {
        final String password = customer.getPassword();
        final Customer existingCustomer;
        existingCustomer = customerDao.getCustomerByContactNumber(customer.getContactNumber());
        if (existingCustomer != null) {
            throw new SignUpRestrictedException("SGR-001",
                                                "This contact number is already registered! Try other contact number.");
        }
        if (!validateCustomer(customer)) {
            throw new SignUpRestrictedException("SGR-005",
                                                "Except last name all fields should be filled");
        }
        if (!validateEmail(customer.getEmail())) {
            throw new SignUpRestrictedException("SGR-002",
                                                "Invalid email-id format!");
        }
        if (!validateContactNumber(customer.getContactNumber())) {
            throw new SignUpRestrictedException("SGR-003",
                                                "Invalid contact number!");
        }
        
        if (!validatePassword(customer.getPassword())) {
            throw new SignUpRestrictedException("SGR-004",
                                                "Weak password!");
        }
        //Encrypt the password
        final String[] encryptedText = passwordCryptographyProvider.encrypt(customer.getPassword());
        customer.setSalt(encryptedText[0]);
        customer.setPassword(encryptedText[1]);
        return customerDao.createCustomer(customer);

    }

    /**
     * Login for a customer
     *
     * @param authorization
     * @return CustomerAuthEntity with login details
     * @throws AuthenticationFailedException
     */
    @Transactional
    public CustomerAuth login(final String authorization)
            throws AuthenticationFailedException {
        final String base64EncodedCredentials = authorization.split("Basic ")[1];
        if (base64EncodedCredentials == null || StringUtils.isEmpty(base64EncodedCredentials)) {
            throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");
        }
        final byte[] decode = Base64.getDecoder().decode(base64EncodedCredentials);
        final String decodedText = new String(decode);
        final String[] decodedArray = decodedText.split(":");
        final String contactNumber = decodedArray[0];
        final String password = decodedArray[1];
        if (password == null || StringUtils.isEmpty(password) || contactNumber == null || StringUtils.isEmpty(contactNumber)) {
            throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");
        }
        final Customer customer = customerDao.getCustomerByContactNumber(contactNumber);
        if (customer == null) {
            throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
        }

        final String encryptedPassword = PasswordCryptographyProvider
                .encrypt(password, customer.getSalt());
        if (encryptedPassword.equals(customer.getPassword())) {
            final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            final CustomerAuth customerAuthToken = new CustomerAuth();
            customerAuthToken.setCustomer(customer);
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);
            customerAuthToken.setAccessToken(
                    jwtTokenProvider.generateToken(customer.getUuid(), now, expiresAt));
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
     * @return The CustomerAuthEntity
     * @throws AuthorizationFailedException
     */
    @Transactional
    public CustomerAuth logout(final String accessToken) throws AuthorizationFailedException {
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
        customerAuth.setLogoutAt(now);
        customerDao.updateCustomerAuthEntity(customerAuth);
        return customerAuth;
    }


    /**
     * This method updates the details of a customer
     *
     * @param accessToken
     * @param customer
     * @return the updated customer entity
     * @throws AuthorizationFailedException
     * @throws UpdateCustomerException
     */
    @Transactional
    public Customer update(final String accessToken, final Customer customer) throws AuthorizationFailedException, UpdateCustomerException {
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
        if (customer.getFirstName() == null || StringUtils.isEmpty(customer.getFirstName())) {
            throw new UpdateCustomerException("UCR-002", "First name field should not be empty");
        }
        final Customer updatedCustomer = customerAuth.getCustomer();
        updatedCustomer.setFirstName(customer.getFirstName());
        updatedCustomer.setLastName(customer.getLastName());
        customerDao.updateCustomer(updatedCustomer);
        return updatedCustomer;
    }

    /**
     * This method updates a password for a customer
     *
     * @param accessToken
     * @param oldPassword
     * @param newPassword
     * @return the updated customer entity with new password
     * @throws AuthorizationFailedException
     * @throws UpdateCustomerException
     * @throws SignUpRestrictedException
     */
    @Transactional
    public Customer updatePassword(final String accessToken, final String oldPassword, final String newPassword) throws AuthorizationFailedException, UpdateCustomerException, SignUpRestrictedException {
        if (oldPassword == null || StringUtils.isEmpty(oldPassword) || newPassword == null || StringUtils.isEmpty(newPassword)) {
            throw new UpdateCustomerException("UCR-003", "No field should be empty");
        }
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
        final Customer customer = customerAuth.getCustomer();
        final String[] encryptedText = passwordCryptographyProvider.encrypt(oldPassword);
        // If encrypted password stored in database and encrypted password from request do not match , throw exception
        if (!StringUtils.equals(encryptedText[1], oldPassword)) {
            throw new UpdateCustomerException("UCR-004", "Incorrect old password");
        }
        if (!validatePassword(newPassword)) {
            throw new UpdateCustomerException("UCR-001", "Weak password!");
        }
        final String[] encryptedTextForNewPassword = passwordCryptographyProvider.encrypt(newPassword);
        customer.setPassword(encryptedTextForNewPassword[1]);
        customer.setSalt(encryptedTextForNewPassword[0]);
        customerDao.updateCustomer(customer);
        return customer;
    }


    /**
     * Validates a customer entity object
     *
     * @param customer
     * @return false when any field other than last name is empty
     */
    private boolean validateCustomer(final Customer customer) {
        if (Objects.isNull(customer.getFirstName()) || StringUtils.isEmpty(customer.getFirstName())) {
            return false;
        } else if (Objects.isNull(customer.getEmail()) || StringUtils.isEmpty(customer.getEmail())) {
            return false;
        } else if (Objects.isNull(customer.getPassword()) || StringUtils.isEmpty(customer.getPassword())) {
            return false;
        } else {
            return !Objects.isNull(customer.getContactNumber()) && !StringUtils.isEmpty(customer.getContactNumber());
        }
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

