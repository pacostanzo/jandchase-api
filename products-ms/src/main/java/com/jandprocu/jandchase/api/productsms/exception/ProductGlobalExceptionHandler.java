package com.jandprocu.jandchase.api.productsms.exception;

import com.jandprocu.jandchase.api.productsms.model.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class ProductGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductNotCreatedException.class)
    public final ResponseEntity<ErrorMessage> notCreatedHandler(ProductNotCreatedException ex, WebRequest request) {
        List<String> message = new ArrayList<>();
        message.add(ex.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(new Date(), message,
                request.getDescription(false));
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.CONFLICT);
    }


    @ExceptionHandler
    public final ResponseEntity<ErrorMessage> notFoundHandler(ProductNotFoundException ex, WebRequest request) {
        List<String> message = new ArrayList<>();
        message.add(ex.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(new Date(), message,
                request.getDescription(false));
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ProductNotUpdatedException.class, IllegalArgumentException.class})
    public final ResponseEntity<ErrorMessage> handleConflict(Throwable ex, WebRequest request) {
        List<String> message = new ArrayList<>();
        message.add(ex.getMessage());
        ErrorMessage errorMessage = new ErrorMessage(new Date(), message,
                request.getDescription(false));
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

}
