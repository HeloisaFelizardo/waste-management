package com.reciclamais.waste_management.exceptions;

public class WastePersistenceException extends RuntimeException {
    public WastePersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
} 