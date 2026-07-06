package com.kish.financeapp.Accounts.exceptions;

public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException(String message){
        super(message);
    }
}
