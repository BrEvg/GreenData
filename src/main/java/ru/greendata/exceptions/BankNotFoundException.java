package ru.greendata.exceptions;
 
public class BankNotFoundException extends Exception {
     
    private static final long serialVersionUID = -3128681006635769411L;
     
    public BankNotFoundException(String message) {
        super(message);
    }
     
}