package com.health.app.exception;

public class ApplicationException extends RuntimeException{

    public ApplicationException(String message){
        super(message);
    }

    public ApplicationException(String message, Throwable cause){
        super(message, cause);
    }
}
