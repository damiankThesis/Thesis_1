package com.praca.komis.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ErrorHandling {

    @ExceptionHandler({NoSuchElementException.class})
    @ResponseBody
    public ResponseEntity<?> noSuchElementException(NoSuchElementException ex, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new DefaultErrorsDTO(
                    new Date(),
                    HttpStatus.NOT_FOUND.value(),
                    HttpStatus.NOT_FOUND.getReasonPhrase(),
                    "Expected resource does not exist!",
                    request.getRequestURI()
            ));
    }
}
