package com.shivendra.codeserver.exception.handler;

import com.shivendra.codeserver.exception.NotFoundException;
import com.shivendra.codeserver.exception.ResourceAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus (HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(NotFoundException e, WebRequest request) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());
        return response;
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    @ResponseStatus (HttpStatus.CONFLICT)
    public Map<String, String> handleConflict(ResourceAlreadyExistsException e, WebRequest request) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getLocalizedMessage());
        return response;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest(IllegalArgumentException e, WebRequest request) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Bad request.");
        return response;
    }


}
