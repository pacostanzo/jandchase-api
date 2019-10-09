package com.jandprocu.jandchase.api.usersms.exception;

import com.jandprocu.jandchase.api.usersms.model.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class UserGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        //Get all fields errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorMessage errorMessage = new ErrorMessage(new Date(), errors,
                request.getDescription(false));
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UserNotUpdatedException.class)
    public final ResponseEntity<ErrorMessage> notUpdatedHandler(UserNotUpdatedException ex, WebRequest request) {
        List<String> message = new ArrayList<>();
        message.add(ex.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(new Date(), message,
                request.getDescription(false));
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotCreatedException.class)
    public final ResponseEntity<ErrorMessage> notCreatedHandler(UserNotCreatedException ex, WebRequest request) {
        List<String> message = new ArrayList<>();
        message.add(ex.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(new Date(), message,
                request.getDescription(false));
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({UserNotFoundException.class, RoleNotFoundException.class})
    public final ResponseEntity<ErrorMessage> notFoundHandler(RuntimeException ex, WebRequest request) {
        List<String> message = new ArrayList<>();
        message.add(ex.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(new Date(), message,
                request.getDescription(false));
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }


}
