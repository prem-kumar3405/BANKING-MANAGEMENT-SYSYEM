package com.prem.banking_management_system.exceptions;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(String message){
        super(message);

    }
}
