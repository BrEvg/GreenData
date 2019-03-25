package ru.greendata.exceptions;
 
public class DepositNotFoundException extends Exception {
     
    private static final long serialVersionUID = -3123116536635769411L;
     
    public DepositNotFoundException(String message) {
        super(message);
    }
     
}