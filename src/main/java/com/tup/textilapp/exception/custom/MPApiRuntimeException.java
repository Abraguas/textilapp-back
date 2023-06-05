package com.tup.textilapp.exception.custom;

public class MPApiRuntimeException extends RuntimeException {

    private final Integer statusCode;
    private final String message;


    public MPApiRuntimeException(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
    public Integer getStatusCode() {
        return statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
