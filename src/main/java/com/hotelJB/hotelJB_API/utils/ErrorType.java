package com.hotelJB.hotelJB_API.utils;

import org.springframework.http.HttpStatus;

public enum ErrorType {
    ENTITY_NOT_FOUND("No se encontró la entidad solicitada", HttpStatus.NOT_FOUND),
    DUPLICATE_ENTITY("La entidad ya existe", HttpStatus.CONFLICT),
    INTERNAL_ERROR("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_AVAILABLE("No disponible", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;

    ErrorType(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}