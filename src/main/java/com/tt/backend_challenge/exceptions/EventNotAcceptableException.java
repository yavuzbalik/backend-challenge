package com.tt.backend_challenge.exceptions;

public class EventNotAcceptableException extends RuntimeException {
    public EventNotAcceptableException(String message) {
        super(message);
    }
}