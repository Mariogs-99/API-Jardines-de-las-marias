package com.hotelJB.hotelJB_API.utils;

public class CustomException extends RuntimeException {
    private final ErrorType errorType;
    private final String entityName;

    public CustomException(ErrorType errorType, String entityName) {
        super(errorType.getMessage() + ": " + entityName);
        this.errorType = errorType;
        this.entityName = entityName;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public String getEntityName() {
        return entityName;
    }
}