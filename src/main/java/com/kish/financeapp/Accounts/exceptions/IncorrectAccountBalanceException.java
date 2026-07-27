package com.kish.financeapp.Accounts.exceptions;

public class IncorrectAccountBalanceException extends RuntimeException {
    public IncorrectAccountBalanceException(String message){
        super(message);
    }
}
