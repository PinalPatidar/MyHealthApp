package com.health.app.exception;

public class DuplicateEmailException extends ApplicationException{

    public DuplicateEmailException(){
        super("An account with this email already exists");
    }
}
