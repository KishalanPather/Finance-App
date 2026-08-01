package com.kish.financeapp.Accounts.exceptions;

public class IncorrectAccountStateException extends RuntimeException {
    public IncorrectAccountStateException(String message){
        super(message);
    }
}
