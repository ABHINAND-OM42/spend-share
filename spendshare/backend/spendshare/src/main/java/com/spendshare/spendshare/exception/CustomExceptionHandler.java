package com.spendshare.spendshare.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException exception){
        return buildErrorResponse(exception.getMessage(),"User validation error");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleDtoValidation(MethodArgumentNotValidException ex){
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        return buildErrorResponse(errorMessage, "Form validation error");
    }

    private ResponseEntity<Object> buildErrorResponse(String message, String errorType){
        Map<String, Object> body = new HashMap<>();

        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error",errorType);
        body.put("message",message);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
