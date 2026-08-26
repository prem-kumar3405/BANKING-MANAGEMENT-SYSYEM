package com.prem.banking_management_system.exceptions;

public class CustomerAlreadyExistsExpection extends RuntimeException{
    public CustomerAlreadyExistsExpection(String message){
        super(message);
    }
}
