package com.ivolodin.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
        log.error("Caught Exception", ex);
        Map<String, Object> body = new LinkedHashMap<>();
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}
