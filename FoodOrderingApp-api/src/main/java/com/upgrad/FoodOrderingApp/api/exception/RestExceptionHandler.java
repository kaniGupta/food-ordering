package com.upgrad.FoodOrderingApp.api.exception;

import com.upgrad.FoodOrderingApp.api.model.ErrorResponse;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {
    
    @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signupRestrictedException(final SignUpRestrictedException exe,
        final WebRequest request) {
        final ErrorResponse error = new ErrorResponse().code(exe.getCode())
                                        .message(exe.getErrorMessage())
                                        .rootCause(exe.getErrorMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationFailedException(final AuthenticationFailedException exe,
        final WebRequest request) {
        final ErrorResponse error = new ErrorResponse().code(exe.getCode())
                                        .message(exe.getErrorMessage())
                                        .rootCause(exe.getErrorMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> authorizationFailedException(final AuthorizationFailedException exe,
        final WebRequest request) {
        final ErrorResponse error = new ErrorResponse().code(exe.getCode())
                                        .message(exe.getErrorMessage())
                                        .rootCause(exe.getErrorMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
    
    @ExceptionHandler(UpdateCustomerException.class)
    public ResponseEntity<ErrorResponse> updateCustomerFailedException(final UpdateCustomerException exe,
        final WebRequest request) {
        final ErrorResponse error = new ErrorResponse().code(exe.getCode())
                                        .message(exe.getErrorMessage())
                                        .rootCause(exe.getErrorMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(SaveAddressException.class)
    public ResponseEntity<ErrorResponse> saveAddressFailedException(final SaveAddressException exe,
        final WebRequest request) {
        final ErrorResponse error = new ErrorResponse().code(exe.getCode())
                                        .message(exe.getErrorMessage())
                                        .rootCause(exe.getErrorMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ErrorResponse> addressNotFoundException(final AddressNotFoundException exe,
        final WebRequest request) {
        final ErrorResponse error = new ErrorResponse().code(exe.getCode())
                                        .message(exe.getErrorMessage())
                                        .rootCause(exe.getErrorMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> categoryNotFoundException(final CategoryNotFoundException exe,
        final WebRequest request) {
        final ErrorResponse error = new ErrorResponse().code(exe.getCode())
                                        .message(exe.getErrorMessage())
                                        .rootCause(exe.getErrorMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(InvalidRatingException.class)
    public ResponseEntity<ErrorResponse> updateCustomerFailedException(final InvalidRatingException exe,
        final WebRequest request) {
        final ErrorResponse error = new ErrorResponse().code(exe.getCode())
                                        .message(exe.getErrorMessage())
                                        .rootCause(exe.getErrorMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<ErrorResponse> categoryNotFoundException(final RestaurantNotFoundException exe,
        final WebRequest request) {
        final ErrorResponse error = new ErrorResponse().code(exe.getCode())
                                        .message(exe.getErrorMessage())
                                        .rootCause(exe.getErrorMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ErrorResponse> couponNotFoundException(final CouponNotFoundException exe,
        final WebRequest request) {
        final ErrorResponse error = new ErrorResponse().code(exe.getCode())
                                        .message(exe.getErrorMessage())
                                        .rootCause(exe.getErrorMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
}


