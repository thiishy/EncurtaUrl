package com.thiimont.encurtaurl.infrastructure;

import com.thiimont.encurtaurl.exception.InactiveUrlException;
import com.thiimont.encurtaurl.exception.InvalidUrlException;
import com.thiimont.encurtaurl.exception.UrlNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<RestErrorMessage> methodArgumentTypeMismatchHandler(MethodArgumentTypeMismatchException exception) {
        RestErrorMessage errorResponse = new RestErrorMessage(HttpStatus.BAD_REQUEST, "O parâmetro especificado é inválido.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestErrorMessage> genericExceptionErrorHandler(Exception exception) {
        RestErrorMessage errorResponse = new RestErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<RestErrorMessage> urlNotFoundErrorHandler(UrlNotFoundException exception) {
        RestErrorMessage errorResponse = new RestErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidUrlException.class)
    public ResponseEntity<RestErrorMessage> invalidUrlErrorHandler(InvalidUrlException exception) {
        RestErrorMessage errorResponse = new RestErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InactiveUrlException.class)
    public ResponseEntity<RestErrorMessage> inactiveUrlErrorHandler(InactiveUrlException exception) {
        RestErrorMessage errorResponse = new RestErrorMessage(HttpStatus.GONE, exception.getMessage());
        return ResponseEntity.status(HttpStatus.GONE).body(errorResponse);
    }

}
