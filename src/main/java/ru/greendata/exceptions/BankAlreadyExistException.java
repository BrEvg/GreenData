package ru.greendata.exceptions;
 
public class BankAlreadyExistException extends Exception {
     
    private static final long serialVersionUID = -3128681626125369411L;
     
    public BankAlreadyExistException(String message) {
        super(message);
    }
     
}