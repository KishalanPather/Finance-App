package com.kish.financeapp.Envelopes.exceptions;

public class EnvelopeNotFoundException extends RuntimeException{
    public EnvelopeNotFoundException(String message){
        super(message);
    }
}
