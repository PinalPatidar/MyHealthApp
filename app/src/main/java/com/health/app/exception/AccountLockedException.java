package com.health.app.exception;

public class AccountLockedException extends ApplicationException{

    public AccountLockedException(String message){
        super("Account is locked");
    }
}
