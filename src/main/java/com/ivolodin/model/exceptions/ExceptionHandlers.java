package com.ivolodin.model.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> globalExceptionHandler(Exception ex) {
        log.error("Caught Exception", ex);
        return new ResponseEntity<>("Error happens", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {PathNotExistException.class})
    public ResponseEntity<Object> pathNotExists(Exception ex) {
        log.debug("Caught Exception", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex) {
        log.debug("Caught Exception", ex);
        Map<String, Object> body = new LinkedHashMap<>();
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<Object> handleBadLogin(BadCredentialsException ex) {
        log.info("Bad credentials", ex);
        HashMap<String, String> body = new HashMap<>();
        body.put("message", "Bad credentials");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    private ResponseEntity<Object> handleArgumentException(Exception ex) {
        log.info("Caught invalid argument", ex);
        HashMap<String, String> body = new HashMap<>();
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = {AccessDeniedException.class})
    private ResponseEntity<Object> handleAccessDeniedException(Exception ex) {
        log.info("Access denied exception", ex);
        HashMap<String, String> body = new HashMap<>();
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}
