package com.kish.financeapp.Envelopes.exceptions;


public class IncorrectEnvelopeBalanceException extends RuntimeException{
    public IncorrectEnvelopeBalanceException(String message){
        super(message);
    }
}
