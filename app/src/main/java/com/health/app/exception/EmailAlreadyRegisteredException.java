package com.health.app.exception;

public class EmailAlreadyRegisteredException extends ApplicationException {

    public EmailAlreadyRegisteredException() {
        super("An account with this email already exists");
    }
}
