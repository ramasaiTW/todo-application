package com.thoughtworks.taskmaster.exceptions;

import com.thoughtworks.taskmaster.dtos.ExceptionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class ControllerAdvices {

    private static final Logger log = LoggerFactory.getLogger(ControllerAdvices.class);

    @ExceptionHandler(DataNotFoundException.class)
    private ResponseEntity<ExceptionDto> handleDataNotFoundException(DataNotFoundException dataNotFoundException) {
        ExceptionDto response = new ExceptionDto(
                OffsetDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                dataNotFoundException.getMessage()
        );
        log.error("Data not Found Exception : {}",dataNotFoundException.getMessage());
        return new ResponseEntity<ExceptionDto>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailExistsException.class)
    private ResponseEntity<ExceptionDto> handleDataNotFoundException(EmailExistsException emailExistsException) {
        ExceptionDto response = new ExceptionDto(
                OffsetDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                emailExistsException.getMessage()
        );
        log.error("Email Exists Exception : {}",emailExistsException.getMessage());
        return new ResponseEntity<ExceptionDto>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProjectAlreadyExistsException.class)
    private ResponseEntity<ExceptionDto> handleDataNotFoundException(ProjectAlreadyExistsException projectAlreadyExistsException) {
        ExceptionDto response = new ExceptionDto(
                OffsetDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                projectAlreadyExistsException.getMessage()
        );
        log.error("Project Already Exists: {}",projectAlreadyExistsException.getMessage());
        return new ResponseEntity<ExceptionDto>(response, HttpStatus.CONFLICT);
    }
}
