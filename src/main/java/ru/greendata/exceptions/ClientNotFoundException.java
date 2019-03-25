package ru.greendata.exceptions;
 
public class ClientNotFoundException extends Exception {
     
    private static final long serialVersionUID = -3120181200612369411L;
     
    public ClientNotFoundException(String message) {
        super(message);
    }
     
}