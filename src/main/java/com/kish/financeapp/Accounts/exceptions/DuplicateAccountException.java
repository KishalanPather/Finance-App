package com.kish.financeapp.Accounts.exceptions;

public class DuplicateAccountException extends RuntimeException {
    public DuplicateAccountException(String message){
        super(message);
    }
}
