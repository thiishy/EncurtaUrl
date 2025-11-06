package com.thiimont.encurtaurl.infrastructure;

import com.thiimont.encurtaurl.exception.InvalidUrlException;
import com.thiimont.encurtaurl.exception.UrlNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<RestErrorMessage> urlNotFoundErrorHandler(UrlNotFoundException exception) {
        RestErrorMessage errorResponse = new RestErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidUrlException.class)
    public ResponseEntity<RestErrorMessage> InvalidUrlErrorHandler(InvalidUrlException exception) {
        RestErrorMessage errorResponse = new RestErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
