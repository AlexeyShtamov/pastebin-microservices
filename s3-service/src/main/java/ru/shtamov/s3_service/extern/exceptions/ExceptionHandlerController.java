package ru.shtamov.s3_service.extern.exceptions;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {


    @ExceptionHandler(SdkClientException.class)
    public ResponseEntity<String> handleResourceNotCreatedException(SdkClientException ex) {
        Error error = new Error(ex.getMessage());
        log.error("Error creating client for Object Storage via AWS SDK. Reason: {}", ex.getMessage());

        return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AmazonS3Exception.class)
    public ResponseEntity<String> handleResourceNotCreatedException(AmazonS3Exception ex) {
        Error error = new Error(ex.getMessage());
        log.error("Error uploading photos to Object Storage. Reason: {}", ex.getMessage());

        return new ResponseEntity<>(error.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TextIsNotFoundException.class)
    public ResponseEntity<String> handleTextIsNotFoundException(TextIsNotFoundException ex) {
        Error error = new Error(ex.getMessage());
        log.error(ex.getMessage());

        return new ResponseEntity<>(error.getMessage(), HttpStatus.NOT_FOUND);
    }

}
