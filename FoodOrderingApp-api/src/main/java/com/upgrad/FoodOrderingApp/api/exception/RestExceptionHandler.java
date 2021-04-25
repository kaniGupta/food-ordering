package com.upgrad.FoodOrderingApp.api.exception;

import com.upgrad.FoodOrderingApp.api.model.ErrorResponse;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
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
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
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
    public ResponseEntity<ErrorResponse> authenticationFailedException(final AuthorizationFailedException exe,
        final WebRequest request) {
        final ErrorResponse error = new ErrorResponse().code(exe.getCode())
                                        .message(exe.getErrorMessage())
                                        .rootCause(exe.getErrorMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(UpdateCustomerException.class)
    public ResponseEntity<ErrorResponse> authenticationFailedException(final UpdateCustomerException exe,
        final WebRequest request) {
        final ErrorResponse error = new ErrorResponse().code(exe.getCode())
                                        .message(exe.getErrorMessage())
                                        .rootCause(exe.getErrorMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}


