package com.example.exception;

public class InvalidCredentialException extends Exception{
    public InvalidCredentialException(String message){
        super(message);
    }
}
