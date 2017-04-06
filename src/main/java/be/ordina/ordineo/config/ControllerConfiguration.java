package be.ordina.ordineo.config;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.NestedServletException;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ControllerConfiguration {


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value= HttpStatus.BAD_REQUEST,reason = "Invalid data sent to server")
    public void constraintsNotValid(){}

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(value=HttpStatus.CONFLICT, reason = "Username is already taken")
    public void usernameIsNotUnique(){}

    @ExceptionHandler(org.springframework.transaction.TransactionSystemException.class)
    @ResponseStatus(value=HttpStatus.BAD_REQUEST,reason = "Invalid data sent to server")
    public void updateConstraintsNotValid(){}

}
