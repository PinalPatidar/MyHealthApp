package com.health.app.exception;

public class InvalidCredentialsException extends ApplicationException {

    public InvalidCredentialsException(String message) {
        super("Invalid email or password");
    }

}
