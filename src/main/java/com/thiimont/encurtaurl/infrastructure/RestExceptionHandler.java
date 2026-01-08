package com.thiimont.encurtaurl.infrastructure;

import com.thiimont.encurtaurl.exception.InactiveUrlException;
import com.thiimont.encurtaurl.exception.InvalidUrlException;
import com.thiimont.encurtaurl.exception.ResourceCreationException;
import com.thiimont.encurtaurl.exception.UrlNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.UNPROCESSABLE_ENTITY.name());
        exception.getBindingResult().getFieldErrors().forEach(error -> errorResponse.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<RestErrorMessage> methodArgumentTypeMismatchHandler(MethodArgumentTypeMismatchException exception) {
        RestErrorMessage errorResponse = new RestErrorMessage(LocalDateTime.now(), HttpStatus.BAD_REQUEST.name(), "O parâmetro especificado é inválido.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ResourceCreationException.class)
    public ResponseEntity<RestErrorMessage> resourceCreationErrorHandler(ResourceCreationException exception) {
        RestErrorMessage errorResponse = new RestErrorMessage(LocalDateTime.now(), HttpStatus.UNPROCESSABLE_ENTITY.name(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<RestErrorMessage> urlNotFoundHandler(UrlNotFoundException exception) {
        RestErrorMessage errorResponse = new RestErrorMessage(LocalDateTime.now(), HttpStatus.NOT_FOUND.name(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidUrlException.class)
    public ResponseEntity<RestErrorMessage> invalidUrlHandler(InvalidUrlException exception) {
        RestErrorMessage errorResponse = new RestErrorMessage(LocalDateTime.now(), HttpStatus.BAD_REQUEST.name(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InactiveUrlException.class)
    public ResponseEntity<RestErrorMessage> inactiveUrlHandler(InactiveUrlException exception) {
        RestErrorMessage errorResponse = new RestErrorMessage(LocalDateTime.now(), HttpStatus.GONE.name(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.GONE).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestErrorMessage> genericErrorHandler(Exception exception) {
        RestErrorMessage errorResponse = new RestErrorMessage(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.name(), "Erro interno do servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}
