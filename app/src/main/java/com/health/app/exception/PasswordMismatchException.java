package com.health.app.exception;

public class PasswordMismatchException extends ApplicationException{

    public PasswordMismatchException() {
        super("Password and confirm password do not match");
    }
}
