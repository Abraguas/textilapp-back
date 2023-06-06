package com.tup.textilapp.exception.custom;

public class InsufficientStockException extends RuntimeException {

    private final String message;

    public InsufficientStockException( String message) {
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}