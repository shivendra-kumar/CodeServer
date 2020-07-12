package com.shivendra.codeserver.exception;

public class ResourceAlreadyExistsException extends RuntimeException{

    private static final String RESOURCE_ALREADY_EXISTS = "Resource with same details already exists.";

    public ResourceAlreadyExistsException() {
        super(RESOURCE_ALREADY_EXISTS);
    }
}
