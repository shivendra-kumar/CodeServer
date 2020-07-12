package com.shivendra.codeserver.exception;

public class NotFoundException extends RuntimeException{

    private static final String NOT_FOUND_MSG = "Resource %s with Id %s not found.";

    public NotFoundException(Class<?> clazz, long id) {
        super(String.format(NOT_FOUND_MSG,clazz.getName(),id));
    }
}
