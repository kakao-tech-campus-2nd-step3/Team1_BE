package com.example.team1_be.util.errorException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class CustomExceptionHandler {

    //    BaseHandler 예외처리

    @ExceptionHandler(ResponseStatusException.class)
    public final ResponseEntity<StatusResponse> handleResponseStatusException(
        ResponseStatusException ex, WebRequest request) {
        StatusResponse errorResponse = new StatusResponse(ex.getStatusCode().value(),
            ex.getReason());
        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }


    //    @Valid로 검출 예외처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<StatusResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException ex, WebRequest request) {
        StatusResponse errorResponse = new StatusResponse(ex.getStatusCode().value(),
            ex.getBindingResult().getFieldError().getDefaultMessage());
        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }

}
