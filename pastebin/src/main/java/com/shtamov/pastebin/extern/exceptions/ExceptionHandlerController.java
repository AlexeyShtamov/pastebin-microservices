package com.shtamov.pastebin.extern.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {


    @ExceptionHandler(TextIsNotCreatedException.class)
    public ResponseEntity<String> handleResourceNotCreatedException(TextIsNotCreatedException ex) {
        Error error = new Error(ex.getMessage());
        log.error(ex.getMessage());

        return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(TextIsNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(TextIsNotFoundException ex) {
        Error error = new Error(ex.getMessage());
        log.error(ex.getMessage());

        return new ResponseEntity<>(error.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectDataException.class)
    public ResponseEntity<String> handleResourceIncorrectDataException(IncorrectDataException ex) {
        Error error = new Error(ex.getMessage());

        log.error(ex.getMessage());

        return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Error error = new Error(ex.getMessage());

        log.error(ex.getMessage());

        return new ResponseEntity<>(error.getMessage(), HttpStatus.NOT_FOUND);
    }
}
